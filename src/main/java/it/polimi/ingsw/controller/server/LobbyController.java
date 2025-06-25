package it.polimi.ingsw.controller.server;

import it.polimi.ingsw.controller.ThreadSafeMessageQueue;
import it.polimi.ingsw.controller.server.connections.VirtualServer;
import it.polimi.ingsw.message.client.ClientMessage;
import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.client.ClientGameListEntry;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.LoggerLevel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Represents a controller in charge of controlling a single game instance, its players, the serialization of it, and the messages regarding it.
 */
public class LobbyController extends Thread implements VirtualServer {

	private final int id;

	private final HashMap<String, ClientDescriptor> listeners;
	private final HashMap<String, Player> disconnected_usernames;
	private final Object listeners_lock;
	private final ThreadSafeMessageQueue<ServerMessage> queue;
	private final String serializer_path;
	private final Object model_lock;
	private ModelInstance model;
	private Timer dsctimer = null;

	/**
	 * Constructs a new {@code LobbyController} object without a ModelInstance tied to it.
	 * 
	 * @param id ID to which the lobby saves its data.
	 */
	public LobbyController(int id) {
		if (id < 0) throw new IllegalArgumentException();
		this.listeners = new HashMap<>();
		this.disconnected_usernames = new HashMap<>();
		this.listeners_lock = new Object();
		this.queue = new ThreadSafeMessageQueue<>(100);
		this.id = id;
		this.model_lock = new Object();
		this.serializer_path = "gtunfinished-" + this.id + ".gtuf";
	}

	public int getID() {
		return this.id;
	}

	/**
     * Main loop of the LobbyController class, retrieves messages from the queue and processes them.
     */
	@Override
	public void run() {
		if (model == null) throw new NullPointerException();
		boolean running = true;
		while (running) {
			try {
				ServerMessage mess = queue.take();
				synchronized (model) {
					mess.receive(this);
					running = this.model.getState() != null;
				}
			} catch (ForbiddenCallException e) {
				Logger.getInstance().print(LoggerLevel.WARN, e.getMessage());
			} catch (InterruptedException e) {
				Logger.getInstance().print(LoggerLevel.NOTIF, "Shutting down lobby " + this.id + " thread!");
			}
		}
		this.endGame();
	}

	/**
     * Takes a {@code ServerMessage} and inserts it into the queue.
     *
     * @param message {@link it.polimi.ingsw.message.server.ServerMessage} Message to be added to the queue.
     */
	public void receiveMessage(ServerMessage message) {
		if (message.getDescriptor() == null || !this.listeners.containsKey(message.getDescriptor().getUsername())) {
			Logger.getInstance().print(LoggerLevel.WARN, "Recieved a message from a client not properly connected!");
			this.broadcast(new ViewMessage("Recieved a message from a client not properly connected!"));
			return;
		}
		this.queue.insert(message);
	}

	/**
     * Takes a {@code ClientMessage} and sends it to each connected listener connected to the lobby.
     *
     * @param message {@link ClientMessage} Message to be broadcast.
     */
	public void broadcast(ClientMessage message) {
		synchronized (listeners_lock) {
			for (ClientDescriptor listener : this.listeners.values()) {
				this.sendMessage(listener, message);
			}
		}
	}

	/**
     * Takes a {@code ClientMessage} and sends it to a specific client.
     *
     * @param message {@link ClientMessage} Message to be sent.
     */
	public void sendMessage(ClientDescriptor client, ClientMessage message) {
		try {
			client.sendMessage(message);
		} catch (IOException e) {
			MainServerController.getInstance().disconnect(client);
		}
	}

	public ModelInstance getModel() throws ForbiddenCallException {
		return this.model;
	}

	/**
	 * Sets a new {@code ModelInstance} to be acted upon by the controller
	 * 
	 * @param model {@link it.polimi.ingsw.model.ModelInstance} New Model to be acted upon. 
	 */
	public void setModel(ModelInstance model) {
		this.model = model;
	}

	/**
	 * Serializes the current game into a file.
	*/
	public void serializeCurrentGame() {
		synchronized (model_lock) {
			try (FileOutputStream file = new FileOutputStream(this.serializer_path);
				 ObjectOutputStream oos = new ObjectOutputStream(file)) {
				oos.reset();
				oos.writeObject(this.model);
				oos.reset();
			} catch (IOException e) {
				e.printStackTrace();
				Logger.getInstance().print(LoggerLevel.ERROR, "Failed to serialize the current modelinstance, closing server!");
				this.endGame();
			}
		}
	}

	/**
	 * Ends any process tied to the model and deletes the save file in case of a successfully finished game.
	*/
	public void endGame() {
		Logger.getInstance().print(LoggerLevel.NOTIF, "Game id: [" + this.id + "] finished!");
		if (this.model.getState() == null) {
			File f = new File(this.serializer_path);
			f.delete();
		}
		MainServerController s = MainServerController.getInstance();
		for (var e : this.listeners.values()) {
			s.joinFromClosedLobby(e);
		}
		for (var e : this.disconnected_usernames.keySet()) {
			s.removeDisconnected(e);
		}
		s.lobbyCloseCleanup(this.id);
	}

	/**
	 * Returns a new TimerTask that skips to the end of the match after a set amount of time, used to automatically finalize a lobby when the server is left with only one player for too long.
	 * 
	 * @param controller {@link it.polimi.ingsw.controller.server.LobbyController} Controller instance.
	 */
	private TimerTask getEndMatchTask(LobbyController controller) {
		return new TimerTask() {
			public void run() {
				Logger.getInstance().print(LoggerLevel.LOBCN, "[" + id + "] " + "Only one player was left for too long, skipping to results screen!");
				broadcast(new ViewMessage("Game timed out due to too few players, skipping to results screen!"));
				model.cutShort();
			}
		};
	}

	/**
     * Connects a {@code ClientDescriptor} to the Lobby and notifies the {@code ModelInstance} if necessary.
     *
     * @param client {@link it.polimi.ingsw.controller.server.ClientDescriptor} Client being connected.
	 * @throws ForbiddenCallException if the model refused the connection.
     */
	public void connect(ClientDescriptor client) throws ForbiddenCallException {
		boolean reconnect = false;
		synchronized (listeners_lock) {
			if (this.listeners.containsKey(client.getUsername())) {
				Logger.getInstance().print(LoggerLevel.LOBCN, "[" + this.id + "] " + "Client '" + client.getUsername() + "' attempted to connect twice!");
				return;
			} else if (this.disconnected_usernames.containsKey(client.getUsername())) {
				client.bindPlayer(this.disconnected_usernames.get(client.getUsername()));
				this.disconnected_usernames.remove(client.getUsername());
				reconnect = true;
			}
			this.listeners.put(client.getUsername(), client);
		}
		synchronized (model_lock) {
			if (!model.getStarted()) {
				Logger.getInstance().print(LoggerLevel.LOBCN, "[" + this.id + "] " + "Client '" + client.getUsername() + "' connected to waiting room!");
				this.model.connect(client);
			} else if (reconnect) {
				broadcast(new ViewMessage("Player: '"+client.getUsername()+"' reconnected!"));
				this.model.connect(client.getPlayer());
				this.model.unpauseGame();
				if (dsctimer != null) {
					this.dsctimer.cancel();
					this.dsctimer = null;
				}
			} else {
				Logger.getInstance().print(LoggerLevel.LOBCN, "[" + this.id + "] " + "Client '" + client.getUsername() + "' started spectating!");
				this.sendMessage(client, new NotifyStateUpdateMessage(model.getState().getClientState()));
			}
		}
		if (reconnect) this.sendMessage(client, new NotifyStateUpdateMessage(model.getState().getClientState()));
	}

	/**
     * Notifies the {@code MainServerController} of a {@code ServerDisconnectMessage} sent by the {@code ClientDescriptor}
     *
     * @param client {@link it.polimi.ingsw.controller.server.ClientDescriptor} Client being disconnected.
     */
	public void disconnect(ClientDescriptor client) {
		MainServerController.getInstance().disconnect(client);
	}

	/**
     * Properly disconnects the {@code ClientDescriptor} from the Model and removes from the local listener list.
     *
     * @param client {@link it.polimi.ingsw.controller.server.ClientDescriptor} Client being disconnected.
     */
	public void disconnectProcedure(ClientDescriptor client) {
		MainServerController s = MainServerController.getInstance();
		synchronized (listeners_lock) {
			if (!listeners.containsKey(client.getUsername())) {
				Logger.getInstance().print(LoggerLevel.WARN, "Client '" + client.getUsername() + "' tried disconnecting from a lobby he was never connected to!");
				return;
			}
			this.listeners.remove(client.getUsername());
			if (client.getPlayer() != null) {
				broadcast(new ViewMessage("Player: '"+client.getUsername()+"' disconnected!"));
				this.disconnected_usernames.put(client.getUsername(), client.getPlayer());
				s.addDisconnected(client.getUsername(), this.id);
			}
			if (this.disconnected_usernames.size() == this.model.getState().getCount().getNumber()) {
				this.dsctimer.cancel();
				this.endGame();
				return;
			}
			if (this.disconnected_usernames.size() >= this.model.getState().getCount().getNumber() - 1) {
				Logger.getInstance().print(LoggerLevel.LOBCN, "Lobby [" + this.id + "] has only one player left, starting timer and pausing game, if nobody joins, game's over!");
				this.broadcast(new ViewMessage("Lobby [" + this.id + "] has only one player left, starting timer and pausing game, if nobody joins, game's over!"));
				this.dsctimer = new Timer(true);
				this.dsctimer.schedule(this.getEndMatchTask(this), 60000L);
				this.model.pauseGame();
			}
		}
		synchronized (model_lock) {
			if (client.getPlayer() != null) {
				this.model.disconnect(client.getPlayer());
			} else if (!this.model.getStarted()) {
				this.model.disconnect(client);
			}
		}
	}

	/**
     * Generates and returns a GameListEntry object containing the lobby details.
     * 
	 * @return {@link it.polimi.ingsw.model.client.ClientGameListEntry} Entry containing the lobby info.
	 */
	public ClientGameListEntry getClientInfo() {
		ClientGameListEntry entry = null;
		synchronized (model_lock) {
			entry = this.model.getEntry();
		}
		return entry;
	}

	/**
     * Refreshes the timeout timer for a particular {@code ClientDescriptor} and restarts the timeout clock belonging to it.
     *
     * @param descriptor {@link it.polimi.ingsw.controller.server.ClientDescriptor} Client pinging the server
     */
	public void ping(ClientDescriptor descriptor) {
		MainServerController.getInstance().ping(descriptor);
	}

}
