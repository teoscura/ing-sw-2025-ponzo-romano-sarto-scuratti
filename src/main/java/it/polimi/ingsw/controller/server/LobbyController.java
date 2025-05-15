package it.polimi.ingsw.controller.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import it.polimi.ingsw.controller.ThreadSafeMessageQueue;
import it.polimi.ingsw.controller.server.connections.VirtualServer;
import it.polimi.ingsw.message.client.ClientMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.client.ClientGameListEntry;
import it.polimi.ingsw.model.player.Player;

public class LobbyController extends Thread implements VirtualServer {

    private final int id;

    private final HashMap<String, ClientDescriptor> listeners;
    private final HashMap<String, Player> disconnected_usernames;
	private final Object listeners_lock;
    private final ThreadSafeMessageQueue<ServerMessage> queue;
    private final String serializer_path;

    private ModelInstance model;
	private final Object model_lock;
	private Timer dsctimer = null;


    public LobbyController(int id){
        if(id<0) throw new IllegalArgumentException();
		this.listeners = new HashMap<>();
		this.disconnected_usernames = new HashMap<>();
		this.listeners_lock = new Object();
		this.queue = new ThreadSafeMessageQueue<>(100);
        this.id = id;
		this.model_lock = new Object();
        this.serializer_path = "gtunfinished-" + this.id + ".gtuf";
    }

	public int getID(){
		return this.id;
	}

	public void setModel(ModelInstance model){
		this.model = model;
	}

    @Override
	public void run() {
		if(model == null) throw new NullPointerException();
		boolean running = true;
		while (running) {
			try {
				ServerMessage mess = queue.poll();
				synchronized(model){
					mess.receive(this);
					running = this.model.getState() == null;
				}
            } catch (ForbiddenCallException e) {
                System.out.println(e.getMessage());
            } catch (InterruptedException e) {
                System.out.println("Force shutdown of lobby "+this.id+" thread!");
            }
		}
        this.endGame();
	}

	public void receiveMessage(ServerMessage message) {
		if (message.getDescriptor() == null || !this.listeners.containsKey(message.getDescriptor().getUsername())) {
			System.out.println("Recieved a message from a client not properly connected!");
			this.broadcast(new ViewMessage("Recieved a message from a client not properly connected!"));
			return;
		}
		this.queue.insert(message);
		return;
	}

	public void broadcast(ClientMessage message) {
		synchronized (listeners_lock) {
			for (ClientDescriptor listener : this.listeners.values()) {
				try {
					listener.sendMessage(message);
				} catch (IOException e) {
					listener.getConnection().close();
					this.disconnect(listener);
				}
			}
		}
	}

    public ModelInstance getModel() throws ForbiddenCallException {
		return this.model;
	}

    public void serializeCurrentGame() {
		synchronized(model_lock){
			try (FileOutputStream file = new FileOutputStream(this.serializer_path);
			ObjectOutputStream oos = new ObjectOutputStream(file)) {
				oos.reset();
				oos.writeObject(this.model);
				oos.reset();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Failed to serialize the current modelinstance, closing server!");
				this.endGame();
			}
		}
	}

    public void endGame(){
		System.out.println("Game id: "+this.id+" finished!");
		if(this.model.getState() == null){
			File f = new File(this.serializer_path);
        	f.delete();
		}
        MainServerController s = MainServerController.getInstance();
        for(var e : this.listeners.values()){
			s.joinFromEndedGame(e);
        }
        for(var e : this.disconnected_usernames.keySet()){
            s.removeDisconnected(e);
        }
		s.gameFinishCleanup(this.id);
    }

	private TimerTask getEndMatchTask(LobbyController controller) {
		return new TimerTask() {
			public void run() {
				System.out.println("Only one player was left for too long, closing the match!");
				controller.endGame();
			}
		};
	}

	//Connections and disconnections
	public void connect(ClientDescriptor client) throws ForbiddenCallException {
		boolean reconnect = false;
		synchronized(listeners_lock){
			if (this.listeners.containsKey(client.getUsername())) {
				System.out.println("Client '" + client.getUsername() + "' attempted to connect twice!");
				return;
			} else if (this.disconnected_usernames.containsKey(client.getUsername())) {
				this.disconnected_usernames.remove(client.getUsername());
				client.bindPlayer(this.disconnected_usernames.get(client.getUsername()));
				reconnect = true;
		    } else {
				this.listeners.put(client.getUsername(), client);
			}
		}
		synchronized (model_lock) {
			if (!model.getStarted()) {
				System.out.println("Client '" + client.getUsername() + "' connected to waiting room!");
				this.model.connect(client);
				return;
			} else if (reconnect){
				this.model.connect(client.getPlayer());
				this.dsctimer.cancel();
				this.dsctimer = null;
			} else {
				System.out.println("Client '" + client.getUsername() + "' started spectating!");
			}
		}
	}

	public void disconnect(ClientDescriptor client) {
		MainServerController s = MainServerController.getInstance();
		synchronized (listeners_lock){
			if (!listeners.containsKey(client.getUsername())) {
				System.out.println("Client '"+client.getUsername()+"' tried disconnecting from a lobby he was never connected to!");
				return;
			}
			System.out.println("Client '" + client.getUsername() + "' disconnected.");
			this.listeners.remove(client.getUsername());
			if (client.getPlayer() != null) {
				this.disconnected_usernames.put(client.getUsername(), client.getPlayer());
				s.addDisconnected(client.getUsername(), this.id);
			}
		}
		synchronized (model_lock) {
			if (client.getPlayer() != null) {
				this.model.disconnect(client.getPlayer());
			} else if (!this.model.getStarted()) {
				this.model.disconnect(client);
			} if(this.model.getState() == null){
				System.out.println("Babbo");
				return;
			}
			if (this.disconnected_usernames.size() >= this.model.getState().getCount().getNumber() - 1) {
				this.dsctimer = new Timer(true);
				this.dsctimer.schedule(this.getEndMatchTask(this), 60000L);
			} else if (this.disconnected_usernames.size() == this.model.getState().getCount().getNumber()) {
				this.dsctimer.cancel();
				this.endGame();
			}
		}
	}

	public ClientGameListEntry getClientInfo() {
		ClientGameListEntry entry = null;
		synchronized(model_lock){
			entry = this.model.getEntry();
		}
		return entry;
	}

}
