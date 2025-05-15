package it.polimi.ingsw.controller.server;

import it.polimi.ingsw.controller.ThreadSafeMessageQueue;
import it.polimi.ingsw.controller.server.connections.*;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.channels.AlreadyConnectedException;
import java.nio.channels.NotYetConnectedException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainServerController extends Thread implements VirtualServer {

	static private MainServerController instance = new MainServerController();

	private final NetworkServer server;
	private final HashMap<String, ClientDescriptor> all_listeners;
	private final HashMap<String, ClientDescriptor> lob_listeners;
	private final HashMap<String, ClientDescriptor> stp_listeners;
	private final HashMap<String, Integer> disconnected;
	private final List<SocketClient> to_setup_tcp;
	private final Object listeners_lock;
	private final ThreadSafeMessageQueue<ServerMessage> queue;
	private final Object lobbies_lock;
	private final Object saved_lock;
	private boolean init = false;
	private int next_id;
	private final HashMap<Integer, LobbyController> lobbies;
	private final HashMap<Integer, ModelInstance> saved;

	private MainServerController() {
		all_listeners = new HashMap<>();
		lob_listeners = new HashMap<>();
		stp_listeners = new HashMap<>();
		disconnected = new HashMap<>();
		to_setup_tcp = new ArrayList<>();
		listeners_lock = new Object();
		queue = new ThreadSafeMessageQueue<>(1000);
		lobbies = new HashMap<>();
		lobbies_lock = new Object();
		saved = new HashMap<>();
		saved_lock = new Object();
		this.updateUnfinishedList();
		this.next_id = this.saved.keySet().stream().max(Integer::compare).orElse(0) + 1;
		server = new NetworkServer();
	}

	static public MainServerController getInstance() {
		if (instance == null) instance = new MainServerController();
		return instance;
	}

	static public void reset() {
		instance = null;
	}

	public void init(String address, int tcpport, int rmiport) {
		if (this.init) throw new AlreadyConnectedException();
		this.server.init(address, tcpport, rmiport);
		this.init = true;
	}

	// -------------------------------------------------------------
	// Message handling, reception and sending.
	// -------------------------------------------------------------

	@Override
	public void run() {
		if (!this.init) throw new NotYetConnectedException();
		this.server.start();
		while (true) {
			try {
				queue.take().receive(this);
			} catch (ForbiddenCallException e) {
				/*XXX*/System.out.println(e.getMessage());
			} catch (InterruptedException e) {
				/*XXX*/System.out.println("Shutting down server!");
			}
		}
	}

	public void receiveMessage(ServerMessage message) {
		if (message.getDescriptor() == null) {
			/*XXX*/System.out.println("Received a message from a Client: not properly connected!");
			this.broadcast(new ViewMessage("Received a message from a Client: not properly connected!"));
			return;
		}
		synchronized (listeners_lock) {
			if (!this.all_listeners.containsKey(message.getDescriptor().getUsername())) {
				/*XXX*/System.out.println("Received a message from a Client: not properly connected!");
				this.broadcast(new ViewMessage("Received a message from a Client: not properly connected!"));
				return;
			}
		}
		if (message.getDescriptor().getId() == -1) {
			this.queue.insert(message);
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
				if (stp_listeners.containsKey(c.getUsername())) continue;
				this.sendMessage(c, message);
			}
		}
	}

	public void sendMessage(ClientDescriptor client, ClientMessage message) {
		try {
			client.sendMessage(message);
		} catch (IOException e) {
			this.disconnect(client);
		}
	}

	//Used to set the correct Client: descriptor for each message getting in.
	public ClientDescriptor getDescriptor(String username) {
		return this.all_listeners.get(username);
	}

	// -------------------------------------------------------------
	// ClientDescriptor connection and disconnection methods.
	// -------------------------------------------------------------

	public void connectListener(SocketClient client) {
		synchronized (listeners_lock) {
			if (this.to_setup_tcp.contains(client)) {
				/*XXX*/System.out.println("A Client: attempted to connect while already connecting!");
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
			/*XXX*/System.out.println("A Client: attempted to change his username after connecting!");
			return;
		}
		this.to_setup_tcp.remove(client);
		client.cancelTimeout();
		if (!this.validateUsername(username)) {
			/*XXX*/System.out.println("A Client: attempted to connect with an invalid name!");
			return;
		}
		/*XXX*/System.out.println("Client: '" + username + "' connected from '" + client.getSocket().getInetAddress() + "'!");
		ClientDescriptor new_listener = new ClientDescriptor(username, client);
		try {
			this.connect(new_listener);
		} catch (ForbiddenCallException e) {
			/*XXX*/System.out.println("Client: '" + username + "' failed to connect!");
			return;
		}
		new_listener.setPingTimerTask(this.timeoutTask(this, new_listener));
	}

	public ClientDescriptor connectListener(RMIClientConnection client) throws RemoteException {
		String name;
		try {
			name = client.getUsername();
		} catch (RemoteException e) {
			throw new RemoteException("Failed to retrieve username from stub");
		}
		ClientDescriptor new_listener = new ClientDescriptor(name, client);
		synchronized (listeners_lock) {

			if (this.all_listeners.containsKey(name) || !validateUsername(name))
				return null;
			try {
				this.connect(new_listener);
			} catch (ForbiddenCallException e) {
				/*XXX*/System.out.println("Client: '" + name + "' failed to connect properly!");
				return null;
			}
			new_listener.setPingTimerTask(this.timeoutTask(this, new_listener));
			return new_listener;
		}
	}

	public VirtualServer getStub(ClientDescriptor new_client) throws RemoteException {
		return new RMIServerStubImpl(this, new_client);
	}

	private boolean validateUsername(String username) {
		Pattern allowed = Pattern.compile("^[a-zA-Z0-9_.-]*$");
		Matcher matcher = allowed.matcher(username);
		return matcher.matches();
	}

	public void connect(ClientDescriptor client) throws ForbiddenCallException {
		//Either add him to a game if hes reconnecting, or add
		int id = -1;
		boolean disconnected = false;
		synchronized (listeners_lock) {
			if (all_listeners.containsKey(client.getUsername())) {
				/*XXX*/System.out.println("Client: '" + client.getUsername() + "' tried to connect twice!");
				return;
			}
			this.all_listeners.put(client.getUsername(), client);
			if (this.disconnected.containsKey(client.getUsername())) {
				id = this.disconnected.get(client.getUsername());
				client.setID(id);
				this.disconnected.remove(client.getUsername());
				disconnected = true;
			} else {
				this.lob_listeners.put(client.getUsername(), client);
				client.setID(-1);
				/*XXX*/System.out.println("Client: '" + client.getUsername() + "' connected!");
				this.sendMessage(client, new NotifyStateUpdateMessage(new ClientLobbySelectState(this.getLobbyList())));
			}
		}
		if (disconnected) {
			synchronized (lobbies_lock) {
				if (this.lobbies.get(id) == null) {
					client.setID(-1);
					return;
				}
				this.lobbies.get(id).connect(client);
				/*XXX*/System.out.println("Client: '" + client.getUsername() + "' reconnected!");
			}
		}
	}

	public void disconnect(ClientDescriptor client) {
		int id = client.getId();
		/*XXX*/System.out.println("Client: '" + client.getUsername() + "' disconnected.");
		if (client.getPingTimerTask() != null) client.getPingTimerTask().cancel();
		synchronized (listeners_lock) {
			if (!all_listeners.containsKey(client.getUsername())) {
				/*XXX*/System.out.println("Client: '" + client.getUsername() + "' disconnected, but was never connected!");
				return;
			}
			this.all_listeners.remove(client.getUsername());
			if (id == -1) {
				this.lob_listeners.remove(client.getUsername());
			}
		}
		try {
			client.getConnection().close();
		} catch (IOException e) {
			e.printStackTrace();
			/*XXX*/System.out.println("Client: '" + client.getUsername() + "' connection closed.");
		}
		if (id == -1) return;
		synchronized (lobbies_lock) {
			var l = this.lobbies.get(id);
			if (l == null) return;
			l.disconnect(client);
		}
	}

	public void addDisconnected(String username, int id) {
		synchronized (listeners_lock) {
			this.disconnected.put(username, id);
		}
	}

	public void removeDisconnected(String username) {
		synchronized (listeners_lock) {
			this.disconnected.remove(username);
		}
	}

	// -------------------------------------------------------------
	// Disconnection Resilience utility.
	// -------------------------------------------------------------

	public void ping(ClientDescriptor client) {
		client.getPingTimerTask().cancel();
		client.setPingTimerTask(this.timeoutTask(this, client));
	}

	public TimerTask TCPTimeoutTask(MainServerController controller, SocketClient client) {
		return new TimerTask() {
			public void run() {
				synchronized (listeners_lock) {
					/*XXX*/System.out.println("Socket '" + client.getSocket().getInetAddress() + "' didn't setup before timing out!");
					to_setup_tcp.remove(client);
				}
			}
		};
	}

	private TimerTask timeoutTask(MainServerController controller, ClientDescriptor client) {
		return new TimerTask() {
			public void run() {
				synchronized (listeners_lock) {
					/*XXX*/System.out.println("Client: '" + client.getUsername() + "' failed to ping in between timeout!");
					controller.disconnect(client);
				}
			}
		};
	}

	// -------------------------------------------------------------
	// Games management utilities and methods.
	// -------------------------------------------------------------

	public int getNext() {
		return this.next_id;
	}

	public void updateUnfinishedList() {
		Pattern saved_game_pattern = Pattern.compile("^gtunfinished-[0-9]+\\.gtuf$");
		File current_directory = new File(".");
		File[] files = current_directory.listFiles();
		if (files == null) throw new NullPointerException();
		ArrayList<File> regex_pass = new ArrayList<>();
		for (File f : files) {
			Matcher matcher = saved_game_pattern.matcher(f.getName());
			if (!matcher.matches()) continue;
			regex_pass.add(f);
		}
		ArrayList<ModelInstance> saved_tmp = new ArrayList<>();
		for (File f : regex_pass) {
			try (FileInputStream fis = new FileInputStream(f.getAbsolutePath());
				 ObjectInputStream ois = new ObjectInputStream(fis)) {
				ModelInstance loaded = (ModelInstance) ois.readObject();
				if (this.lobbies.get(loaded.getID()) != null) continue;
				saved_tmp.add(loaded);
			} catch (IOException e) {
				/*XXX*/System.out.println("Read error during loading of File: '" + f.getName());
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				/*XXX*/System.out.println("File: '" + f.getName() + "' is not a valid savefile!");
				e.printStackTrace();
			}
		}
		synchronized (lobbies_lock) {
			for (ModelInstance m : saved_tmp) {
				if (this.lobbies.get(m.getID()) != null) saved_tmp.remove(m);
			}
		}
		synchronized (saved_lock) {
			this.saved.clear();
			for (ModelInstance m : saved_tmp) {
				this.saved.put(m.getID(), m);
			}
		}
	}

	public void enterSetup(ClientDescriptor client) throws ForbiddenCallException {
		synchronized (listeners_lock) {
			if (!lob_listeners.containsKey(client.getUsername())) {
				/*XXX*/System.out.println("Client: '" + client.getUsername() + "' started setting up a lobby, but he's already playing!");
				return;
			} else if (stp_listeners.containsKey(client.getUsername())) {
				/*XXX*/System.out.println("Client: '" + client.getUsername() + "' started setting up a lobby, but he's already doing that!");
				return;
			}
			this.stp_listeners.put(client.getUsername(), client);
		}
		ClientSetupState state = null;
		ArrayList<ClientGameListEntry> tmp = new ArrayList<>();
		synchronized (saved_lock) {
			for (var m : this.saved.values()) {
				tmp.add(m.getEntry());
			}
		}
		state = new ClientSetupState(client.getUsername(), tmp);
		/*XXX*/System.out.println("Client: '" + client.getUsername() + "' has entered setup state!");
		this.sendMessage(client, new NotifyStateUpdateMessage(state));
	}

	public void leaveSetup(ClientDescriptor client) {
		synchronized (listeners_lock) {
			if (!lob_listeners.containsKey(client.getUsername())) {
				/*XXX*/System.out.println("Client: '" + client.getUsername() + "' tried to leave setup, but he's already playing!");
				return;
			} else if (!stp_listeners.containsKey(client.getUsername())) {
				/*XXX*/System.out.println("Client: '" + client.getUsername() + "' tried to leave setup, but he wasn't never setupping!");
				return;
			}
			this.stp_listeners.remove(client.getUsername());
		}
		/*XXX*/System.out.println("Client: '" + client.getUsername() + "' has left setup state!");
		this.notifyLobbyListeners();
	}

	public void openNewRoom(ClientDescriptor client, GameModeType type, PlayerCount count) throws ForbiddenCallException {
		synchronized (listeners_lock) {
			if (!this.stp_listeners.containsKey(client.getUsername()) && this.lob_listeners.containsKey(client.getUsername())) {
				/*XXX*/System.out.println("Client: '" + client.getUsername() + "' tried opening a lobby, but he's not setupping!");
				return;
			}
			if (!this.lob_listeners.containsKey(client.getUsername())) {
				/*XXX*/System.out.println("Client: '" + client.getUsername() + "' tried opening a lobby, but he's already playing!");
				return;
			}
			this.stp_listeners.remove(client.getUsername());
			this.lob_listeners.remove(client.getUsername());
		}
		/*XXX*/System.out.println("Client: '" + client.getUsername() + "' opened a new lobby! [Type: " + type + " | Size: " + count + "]");
		LobbyController new_lobby = new LobbyController(this.next_id);
		ModelInstance model = new ModelInstance(this.next_id, new_lobby, type, count);
		new_lobby.setModel(model);
		new_lobby.start();
		synchronized (lobbies_lock) {
			this.lobbies.put(this.next_id, new_lobby);
			this.next_id++;
		}
		client.setID(new_lobby.getID());
		new_lobby.connect(client);
		this.notifyLobbyListeners();
	}


	public void openUnfinished(ClientDescriptor client, int id) throws ForbiddenCallException {
		synchronized (listeners_lock) {
			if (!this.stp_listeners.containsKey(client.getUsername()) && this.lob_listeners.containsKey(client.getUsername())) {
				/*XXX*/System.out.println("Client: '" + client.getUsername() + "' tried opening a lobby, but he's not setupping!");
				return;
			}
			if (!this.lob_listeners.containsKey(client.getUsername())) {
				/*XXX*/System.out.println("Client: '" + client.getUsername() + "' tried opening a lobby, but he's already playing!");
				return;
			}
			this.stp_listeners.remove(client.getUsername());
			this.lob_listeners.remove(client.getUsername());
		}
		ModelInstance loaded = null;
		boolean reset = false;
		synchronized (saved_lock) {
			if (!saved.containsKey(id)) {
				/*XXX*/System.out.println("Client: '" + client.getUsername() + "' attempted to open a non-existant saved game!");
				reset = true;
			}
		}
		if (reset) {
			synchronized (listeners_lock) {
				this.stp_listeners.remove(client.getUsername());
				this.lob_listeners.put(client.getUsername(), client);
			}
			return;
		}
		synchronized (saved_lock) {
			loaded = this.saved.get(id);
			if (!loaded.getEntry().getPlayers().contains(client.getUsername())) {
				/*XXX*/System.out.println("Client: '" + client.getUsername() + "' attempted resume a game, but he wasn't playing in it before!");
				return;
			}
		}
		this.updateUnfinishedList();
		/*XXX*/System.out.println("Client: '" + client.getUsername() + "' opened a new lobby! [Type: " + loaded.getState().getType() + " | Size: " + loaded.getState().getCount() + "]");
		LobbyController new_lobby = new LobbyController(id);
		loaded.setController(new_lobby);
		loaded.afterSerialRestart();
		new_lobby.setModel(loaded);
		new_lobby.start();
		synchronized (lobbies_lock) {
			this.lobbies.put(new_lobby.getID(), new_lobby);
		}
		client.setID(new_lobby.getID());
		new_lobby.connect(client);
		this.notifyLobbyListeners();
	}

	public void gameFinishCleanup(int id) {
		synchronized (lobbies_lock) {
			var l = this.lobbies.get(id);
			if (l == null) throw new RuntimeException();
			this.lobbies.remove(id);
			try {
				l.interrupt();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
			/*XXX*/System.out.println("Game [" + id + "] ended, closing its controller!");
		}
		this.updateUnfinishedList();
		this.notifyLobbyListeners();
	}

	public void connectToLobby(ClientDescriptor client, int id) throws ForbiddenCallException {
		synchronized (listeners_lock) {
			if (!all_listeners.containsKey(client.getUsername())) {
				/*XXX*/System.out.println("Client: '" + client.getUsername() + "' attempted to join a lobby, but was never connected!");
				return;
			} else if (client.getId() != -1) {
				/*XXX*/System.out.println("Client: '" + client.getUsername() + "' attempted to join a lobby, but is already playing!");
				return;
			}
			this.lob_listeners.remove(client.getUsername());
		}
		synchronized (lobbies_lock) {
			if (this.lobbies.containsKey(id)) {
				client.setID(id);
				this.lobbies.get(id).connect(client);
			} else {
				/*XXX*/System.out.println("Client: '" + client.getUsername() + "' tried to connect to a non existant lobby! [ID:" + id + "]");
			}
		}
		this.notifyLobbyListeners();
	}

	public void notifyLobbyListeners() {
		ClientLobbySelectState state = new ClientLobbySelectState(this.getLobbyList());
		ClientMessage message = new NotifyStateUpdateMessage(state);
		this.broadcast(message);
	}

	public ArrayList<ClientGameListEntry> getLobbyList() {
		ArrayList<ClientGameListEntry> tmp = new ArrayList<>();
		synchronized (lobbies_lock) {
			for (var l : this.lobbies.values()) {
				tmp.add(l.getClientInfo());
			}
			return tmp;
		}
	}

	public void joinFromEndedGame(ClientDescriptor client) {
		synchronized (listeners_lock) {
			if (this.lob_listeners.containsKey(client.getUsername())) {
				/*XXX*/System.out.println("Client: '" + client.getUsername() + "' is already in lobby!");
				return;
			}
			this.lob_listeners.put(client.getUsername(), client);
		}
		client.setID(-1);
	}

}
