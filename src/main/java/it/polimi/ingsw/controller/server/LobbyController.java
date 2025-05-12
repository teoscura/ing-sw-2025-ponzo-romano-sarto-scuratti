package it.polimi.ingsw.controller.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import it.polimi.ingsw.controller.server.connections.RemoteServer;
import it.polimi.ingsw.message.client.ClientMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.client.ClientGameListEntry;
import it.polimi.ingsw.model.player.Player;

public class LobbyController extends Thread implements RemoteServer {

    private final int id;

    private final HashMap<String, ClientDescriptor> listeners = new HashMap<>();
    private final HashMap<String, Player> disconnected_usernames = new HashMap<>();
	private final Object listeners_lock = new Object();

    private final Queue<ServerMessage> queue = new ArrayDeque<>();
    private final Object queue_lock = new Object();

    private final String serializer_path;
    private ModelInstance model;
	private Timer dsctimer = null;


    public LobbyController(int id){
        if(id<0) throw new IllegalArgumentException();
        this.id = id;
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
		while (model.getState()!=null) {
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
        this.endGame();
	}

	public void receiveMessage(ServerMessage message) {
		if (message.getDescriptor() == null || !this.listeners.containsKey(message.getDescriptor().getUsername())) {
			System.out.println("Recieved a message from a client not properly connected!");
			this.broadcast(new ViewMessage("Recieved a message from a client not properly connected!"));
			return;
		}
		synchronized (queue_lock) {
			this.queue.add(message);
			queue_lock.notifyAll();
		}
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
		synchronized (queue_lock) {
			try (FileOutputStream file = new FileOutputStream(this.serializer_path);
				 ObjectOutputStream oos = new ObjectOutputStream(file)) {
				oos.reset();
				oos.writeObject(this.model);
				oos.reset();
			} catch (IOException e) {
				System.out.println("Failed to serialize the current modelinstance, closing server!");
				this.endGame();
			}
		}
	}

    protected void endGame(){
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
		synchronized (queue_lock) {
			if (!model.getStarted()) {
				System.out.println("Client '" + client.getUsername() + "' connected to waiting room!");
				this.broadcast(new ViewMessage("Client '" + client.getUsername() + "' connected to waiting room!"));
				this.model.connect(client);
				return;
			}
			if (this.listeners.containsKey(client.getUsername())) {
				System.out.println("Client '" + client.getUsername() + "' attempted to connect twice!");
				this.broadcast(new ViewMessage("Client '" + client.getUsername() + "' attempted to connect twice!"));
				return;
			} else if (this.disconnected_usernames.containsKey(client.getUsername())) {
				this.listeners.put(client.getUsername(), client);
				this.disconnected_usernames.remove(client.getUsername());
				client.bindPlayer(this.disconnected_usernames.get(client.getUsername()));
				this.model.connect(client.getPlayer());
				this.dsctimer.cancel();
				this.dsctimer = null;
			} else {
				this.listeners.put(client.getUsername(), client);
			}
		}
	}

	public void disconnect(ClientDescriptor client) {
		MainServerController s = MainServerController.getInstance();
		synchronized (queue_lock) {
			if (!listeners.containsValue(client)) {
				return;
			}
			client.getConnection().close();
			if (client.getPlayer() != null) {
				this.model.disconnect(client.getPlayer());
				this.disconnected_usernames.put(client.getUsername(), client.getPlayer());
				s.addDisconnected(client.getUsername(), this.id);
			}
			this.listeners.remove(client.getUsername());
			System.out.println("Client '" + client.getUsername() + "' disconnected.");
			this.broadcast(new ViewMessage("Client '" + client.getUsername() + "' disconnected."));
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
		synchronized(queue_lock){
			entry = this.model.getEntry();
		}
		return entry;
	}

}
