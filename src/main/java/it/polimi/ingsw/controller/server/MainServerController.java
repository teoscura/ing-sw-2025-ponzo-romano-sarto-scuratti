package it.polimi.ingsw.controller.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.channels.AlreadyConnectedException;
import java.nio.channels.NotYetConnectedException;
import java.rmi.RemoteException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.polimi.ingsw.controller.client.connections.RMIClientStub;
import it.polimi.ingsw.controller.server.connections.RMIServerStubImpl;
import it.polimi.ingsw.controller.server.connections.RemoteServer;
import it.polimi.ingsw.controller.server.connections.SocketClient;
import it.polimi.ingsw.message.client.ClientMessage;
import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.client.ClientGameListEntry;
import it.polimi.ingsw.model.client.state.ClientLobbySelectState;
import it.polimi.ingsw.model.client.state.ClientSetupState;

public class MainServerController extends Thread implements RemoteServer {
    
    static private MainServerController instance = new MainServerController();

    private final Server server;
    private boolean init = false;

    private final HashMap<String, ClientDescriptor> all_listeners;
    private final HashMap<String, ClientDescriptor> lob_listeners;
    private final HashMap<String, ClientDescriptor> stp_listeners;
    private final HashMap<String, Integer> disconnected;
    private final List<SocketClient> to_setup_tcp;
	private final Object listeners_lock;

    private final Queue<ServerMessage> queue;
    private final Object queue_lock;

    private int next_id;
    private HashMap<Integer, LobbyController> lobbies;
    private final Object lobbies_lock;
    private HashMap<Integer, ModelInstance> saved;
    private final Object saved_lock;

    private MainServerController(){
        all_listeners = new HashMap<>();
        lob_listeners = new HashMap<>();
        stp_listeners = new HashMap<>();
        disconnected = new HashMap<>();
        to_setup_tcp = new ArrayList<>();
        listeners_lock = new Object();
        queue = new ArrayDeque<>();
        queue_lock = new Object();
        lobbies = new HashMap<>();
        lobbies_lock = new Object();
        saved = new HashMap<>();
        saved_lock = new Object();
        this.updateUnfinishedList();
        this.next_id = this.saved.keySet().stream().max(Integer::compare).orElse(0) + 1;
        server = new Server();
    }

    static public MainServerController getInstance(){
        return instance;
    }

    public void init(String address, int rmiport){
        if(this.init) throw new AlreadyConnectedException();
        this.server.init(address, rmiport);
        this.init = true;
    }

    // -------------------------------------------------------------
    // Message handling, reception and sending.
    // -------------------------------------------------------------

    @Override
	public void run() {
        if(!this.init) throw new NotYetConnectedException();
        this.server.start();
		while (true) {
			synchronized (queue_lock) {
				while(this.queue.isEmpty()){
					try {
						queue_lock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				while (!queue.isEmpty()) {
					ServerMessage message = this.queue.poll();
					try {
						message.receive(this);
					} catch (ForbiddenCallException e) {
						System.out.println(e.getMessage());
					}
				}
				queue_lock.notifyAll();
			}
		}
	}

    public void receiveMessage(ServerMessage message) {
		if (message.getDescriptor() == null || !this.all_listeners.containsKey(message.getDescriptor().getUsername())) {
			System.out.println("Received a message from a client not properly connected!");
			this.broadcast(new ViewMessage("Received a message from a client not properly connected!"));
			return;
		}
        if (message.getDescriptor().getId() == -1 ) {
            synchronized (queue_lock) {
                this.queue.add(message);
                queue_lock.notifyAll();
            }
            return;
        }	
        var target = this.lobbies.get(message.getDescriptor().getId());
        if (target == null) {
            message.getDescriptor().setID(-1);
        } else {
            target.receiveMessage(message);
        }
	}

    public void broadcast(ClientMessage message) {
		synchronized (listeners_lock) {
			for (ClientDescriptor c : this.lob_listeners.values()) {
				try {
					c.sendMessage(message);
				} catch (IOException e) {
					c.getConnection().close();
					this.disconnect(c);
				}
			}
		}
	}

    public void sendMessage(ClientDescriptor client, ClientMessage message){
        try {
            client.sendMessage(message);
        } catch (IOException e) {
            client.getConnection().close();
            this.disconnect(client);
        }
    }
    
    //Used to set the correct client descriptor for each message getting in.
    public ClientDescriptor getDescriptor(String username) {
		return this.all_listeners.get(username);
	}
    
    // -------------------------------------------------------------
    // ClientDescriptor connection and disconnection methods.
    // -------------------------------------------------------------

    public void connectListener(SocketClient client) {
		synchronized (listeners_lock) {
			if (this.to_setup_tcp.contains(client)) {
				System.out.println("A client attempted to connect while already connecting!");
				return;
			}
			this.to_setup_tcp.add(client);
            TimerTask task = this.TCPTimeoutTask(instance, client);
            Timer t = new Timer(true);
            client.setTimeout(task);
            t.schedule(task, 20000L);
		}
	}

    public void setupSocketListener(SocketClient client, String username) {
		if (!this.to_setup_tcp.contains(client)) {
			System.out.println("A client attempted to change his username after connecting!");
			return;
		}
		this.to_setup_tcp.remove(client);
        client.cancelTimeout();
		if (!this.validateUsername(username)) {
			System.out.println("A client attempted to connect with an invalid name!");
			return;
		}
		ClientDescriptor new_listener = new ClientDescriptor(username, client);
		try {
			this.connect(new_listener);
		} catch (ForbiddenCallException e) {
			System.out.println("Client: '" + username + "' failed to connect!");
			return;
		}
		new_listener.setPingTimerTask(this.timeoutTask(this, new_listener));
	}

	public ClientDescriptor connectListener(RMIClientStub client) {
		synchronized (listeners_lock) {
			ClientDescriptor new_listener = new ClientDescriptor(client.getUsername(), client);
			if (this.all_listeners.containsKey(client.getUsername()) || !validateUsername(client.getUsername()))
				return null;
			try {
				this.connect(new_listener);
			} catch (ForbiddenCallException e) {
				System.out.println("Client '" + client.getUsername() + "' failed to connect properly!");
				return null;
			}
			new_listener.setPingTimerTask(this.timeoutTask(this, new_listener));
			return new_listener;
		}
	}

    public RemoteServer getStub(ClientDescriptor new_client) throws RemoteException {
		return (RemoteServer) new RMIServerStubImpl(this, new_client);
	}

    private boolean validateUsername(String username) {
		Pattern allowed = Pattern.compile("^[a-zA-Z0-9_.-]*$");
		Matcher matcher = allowed.matcher(username);
		return matcher.matches();
	}

    public void connect(ClientDescriptor client) throws ForbiddenCallException {
        //Either add him to a game if hes reconnecting, or add 
        synchronized(listeners_lock){
            this.all_listeners.put(client.getUsername(), client);
            if (this.disconnected.containsKey(client.getUsername())) {
                int id = this.disconnected.get(client.getUsername());
                client.setID(id);
                this.disconnected.remove(client.getUsername());
                this.lobbies.get(id).connect(client);
            } else {
                this.lob_listeners.put(client.getUsername(), client);
            }
        }
    }

    public void disconnect(ClientDescriptor client){
        int id = client.getId();
        synchronized(listeners_lock){
            this.all_listeners.remove(client.getUsername());
            if (id == -1) {
                this.lob_listeners.remove(client.getUsername());
                return;
            }
        }
        synchronized(lobbies_lock){
            var l = this.lobbies.get(id);
            if(l == null) return;
            l.disconnect(client);
        }
    }

    public void addDisconnected(String username, int id){
        synchronized(listeners_lock){
            this.disconnected.put(username, id);
        }
    }

    public void removeDisconnected(String username){
        synchronized(listeners_lock){
            this.disconnected.remove(username);
        }
    }

    // -------------------------------------------------------------
    // Disconnection Resilience utility.
    // -------------------------------------------------------------

    public void ping(ClientDescriptor client) {
		client.setPingTimerTask(this.timeoutTask(this, client));
	}

    public TimerTask TCPTimeoutTask(MainServerController controller, SocketClient client) {
		return new TimerTask() {
			public void run() {
				synchronized (listeners_lock) {
					System.out.println("Socket '" + client.getSocket().getInetAddress() + "' didn't setup before timing out!");
					to_setup_tcp.remove(client);
				}
			}
		};
	}

    private TimerTask timeoutTask(MainServerController controller, ClientDescriptor client) {
		return new TimerTask() {
			public void run() {
				synchronized (queue_lock) {
					System.out.println("Client '" + client.getUsername() + "' failed to ping in between timeout!");
					controller.disconnect(client);
				}
			}
		};
	}

    // -------------------------------------------------------------
    // Games management utilities and methods.
    // -------------------------------------------------------------

    private void updateUnfinishedList(){
        Pattern saved_game_pattern = Pattern.compile("^gtunfinished-[0-9]+\\.gtuf$");
		File current_directory = new File(".");
		File[] files = current_directory.listFiles();
		if(files == null) throw new NullPointerException();
        ArrayList<File> regex_pass = new ArrayList<>();
        for(File f : files){
            Matcher matcher = saved_game_pattern.matcher(f.getName());
            if(!matcher.matches()) continue;
            regex_pass.add(f);
        }
        ArrayList<ModelInstance> saved_tmp = new ArrayList<>();
        for(File f : regex_pass){
            try (FileInputStream fis = new FileInputStream(f.getAbsolutePath());
                    ObjectInputStream ois = new ObjectInputStream(fis)) {
                ModelInstance loaded = (ModelInstance) ois.readObject();
                if(this.lobbies.get(loaded.getID())!=null) continue;
                saved_tmp.add(loaded);
            } catch (IOException e) {
                System.out.println("Read error during loading of File: '" + f.getName());
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                System.out.println("File: '" + f.getName() + "' is not a valid savefile!");
                e.printStackTrace();
            }
        }
        synchronized(lobbies_lock){
            for(ModelInstance m : saved_tmp){
                if(this.lobbies.get(m.getID())!=null) saved_tmp.remove(m);
            }
        }
        synchronized(saved_lock){
            this.saved.clear();
            for(ModelInstance m : saved_tmp){
                this.saved.put(m.getID(), m);
            }
        }
    }

	public void enterSetup(ClientDescriptor client) throws ForbiddenCallException {
        synchronized(listeners_lock){
            if (!this.lob_listeners.containsKey(client.getUsername())) {
                System.out.println("Client '" + client.getUsername() + "' started setting up a lobby, but he's already playing!");
                return;
            }
            if (this.stp_listeners.containsKey(client.getUsername())) {
                System.out.println("Client '" + client.getUsername() + "' started setting up a lobby, but he's already doing that!");
                return;
            }
            this.lob_listeners.remove(client.getUsername());
            this.stp_listeners.put(client.getUsername(), client);
        }
		ClientSetupState state = null;
        ArrayList<ClientGameListEntry> tmp = new ArrayList<>();
        synchronized(saved_lock){
            for(var m : this.saved.values()){
                tmp.add(m.getEntry());
            }
        }
        state = new ClientSetupState(client.getUsername(), tmp);
        this.sendMessage(client, new NotifyStateUpdateMessage(state));
	}

    public void leaveSetup(ClientDescriptor descriptor) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'leaveSetup'");
    }

    public void openNewRoom(ClientDescriptor client, GameModeType type, PlayerCount count) throws ForbiddenCallException {
        synchronized(listeners_lock){
            if (!this.stp_listeners.containsKey(client.getUsername())) {
                System.out.println("Client '" + client.getUsername() + "' attempted open a lobby, but he isn't setting up anything!");
                this.stp_listeners.remove(client.getUsername());
                this.lob_listeners.put(client.getUsername(), client);
                return;
            }
            //XXX check if hes setupping already and if hes not in a lobby. 
        }
        LobbyController new_lobby = new LobbyController(this.next_id);
        ModelInstance model = new ModelInstance(this.next_id, new_lobby, type, count);
        new_lobby.setModel(model);
        new_lobby.start();
        synchronized(lobbies_lock){
            this.lobbies.put(this.next_id, new_lobby);
            this.next_id++;
        }
        this.stp_listeners.remove(client.getUsername());
        new_lobby.connect(client);
        this.notifyLobbyListeners();
	}

	public void openUnfinished(ClientDescriptor client, int id) throws ForbiddenCallException {
        synchronized(listeners_lock){
            if (!this.stp_listeners.containsKey(client.getUsername())) {
                System.out.println("Client '" + client.getUsername() + "' attempted open a lobby, but he isn't setting up anything!");
                return;
            }
            //XXX check if hes setupping already and if hes not in a lobby. 
        }
        ModelInstance loaded = null;
        synchronized(saved_lock){
            if(!saved.containsKey(id)){
                System.out.println("Client '"+client.getUsername()+"' attempted to open a non-existant saved game!");
                this.stp_listeners.remove(client.getUsername());
                this.lob_listeners.put(client.getUsername(), client);
                return;
            }
            loaded = this.saved.get(id);
        }
        this.updateUnfinishedList();
        LobbyController new_lobby = null;
        loaded.afterSerialRestart();
        loaded.setController(new_lobby);
        new_lobby = new LobbyController(id);
        new_lobby.setModel(loaded);
        new_lobby.start();
        synchronized(lobbies_lock){
            this.lobbies.put(this.next_id, new_lobby);
            this.next_id++;
        }
        this.stp_listeners.remove(client.getUsername());
        new_lobby.connect(client);
        this.notifyLobbyListeners();
	}

    public void gameFinishCleanup(int id) {
        synchronized(lobbies_lock){
            var l = this.lobbies.get(id);
            if(l == null) throw new RuntimeException();
            this.lobbies.remove(id);
            try {
                l.join();
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        this.updateUnfinishedList();
        this.notifyLobbyListeners();
    }

    public void connectToLobby(ClientDescriptor client, int id) throws ForbiddenCallException {
        synchronized(listeners_lock){
            if (!all_listeners.containsKey(client.getUsername())) {
                System.out.println("Client '" + client.getUsername() + "' attempted to join a lobby, but was never connected!");
                return;
            }
            else if(client.getId()!=-1) {
                System.out.println("Client '" + client.getUsername() + "' attempted to join a lobby, but is already playing!");
                return;
            }
        }
        synchronized(lobbies_lock){
            client.setID(id);
            this.lobbies.get(id).connect(client); 
        }
        this.notifyLobbyListeners();
    }

    public void notifyLobbyListeners(){
        ClientLobbySelectState state = null;
        ArrayList<ClientGameListEntry> tmp = new ArrayList<>();
        synchronized(lobbies_lock){
            for(var l : this.lobbies.values()){
                tmp.add(l.getClientInfo());
            }
        }
        state = new ClientLobbySelectState(tmp);
        ClientMessage message = new NotifyStateUpdateMessage(state);
        this.broadcast(message);
    }

    //XXX In case someone is kicked back to lobby, send them the lobby list again.

}
