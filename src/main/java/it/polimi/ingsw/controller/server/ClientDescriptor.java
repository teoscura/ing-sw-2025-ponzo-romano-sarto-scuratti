package it.polimi.ingsw.controller.server;

import it.polimi.ingsw.controller.server.connections.ClientConnection;
import it.polimi.ingsw.message.client.ClientMessage;
import it.polimi.ingsw.model.player.Player;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Represents a unique Client connected to the server, can be tied to a {@link it.polimi.ingsw.model.player.Player} object and to a {@link ClientConnection}. 
 */
public class ClientDescriptor {

	private static final long TIMEOUT_DURATION = 15000L;
	private final String username;
	private transient final Timer timer;
	private transient final ClientConnection connection;
	private transient int lobby_id;
	private transient TimerTask task;
	private transient Player player = null;

	/**
	 * Constructs a {@link it.polimi.ingsw.controller.server.ClientDescriptor} object with the provided names and connection.
	 * @param username Username, unique to the client.
	 * @param connection {@link ClientConnection} Connection used to send messages to the client.
	 */
	public ClientDescriptor(String username, ClientConnection connection) {
		if (username == null || connection == null) throw new NullPointerException();
		this.timer = new Timer(true);
		this.username = username;
		this.connection = connection;
		this.lobby_id = -1;
	}

	/**
	 * Bind a {@link it.polimi.ingsw.model.player.Player} object to this descriptor.
	 * 
	 * @param p {@link it.polimi.ingsw.model.player.Player} Player to be linked.
	 */
	public void bindPlayer(Player p) {
		p.bindDescriptor(this);
		this.player = p;
	}

	/**
	 * Sends a {@link ClientMessage} using the {@link ClientConnection} tied to the descriptor.
	 * @param m {@link ClientMessage} Message to be sent.
	 * @throws IOException if there are any connection related issues during the process.
	 */
	public void sendMessage(ClientMessage m) throws IOException {
		this.connection.sendMessage(m);
	}

	public String getUsername() {
		return this.username;
	}

	public int getId() {
		return this.lobby_id;
	}

	/**
	 * Tie descriptor to a new Lobby ID.
	 * 
	 * @param id ID of the new lobby tied to the descriptor.
	 */
	public void setID(int id) {
		if (id < -1) throw new IllegalArgumentException();
		this.lobby_id = id;
	}

	public Player getPlayer() {
		return this.player;
	}

	public TimerTask getPingTimerTask() {
		return this.task;
	}

	/**
	 * Sets a new callback {@link TimerTask} to be executed on timeout.
	 * 
	 * @param task {@link TimerTask} Ping callback task to be called in case of a timeout.
	 */
	public void setPingTimerTask(TimerTask task) {
		this.task = task;
		timer.schedule(task, TIMEOUT_DURATION);
	}

	public ClientConnection getConnection() {
		return this.connection;
	}

}
