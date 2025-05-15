package it.polimi.ingsw.controller.server;

import it.polimi.ingsw.controller.server.connections.ClientConnection;
import it.polimi.ingsw.message.client.ClientMessage;
import it.polimi.ingsw.model.player.Player;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class ClientDescriptor {

	private static final long TIMEOUT_DURATION = 15000L;
	private final String username;
	private transient final Timer timer;
	private transient final ClientConnection connection;
	private transient int id;
	private transient TimerTask task;
	private transient Player player = null;

	public ClientDescriptor(String username, ClientConnection connection) {
		if (username == null || connection == null) throw new NullPointerException();
		this.timer = new Timer(true);
		this.username = username;
		this.connection = connection;
		this.id = -1;
	}

	public void bindPlayer(Player p) {
		p.bindDescriptor(this);
		this.player = p;
	}

	public void sendMessage(ClientMessage m) throws IOException {
		this.connection.sendMessage(m);
	}

	public String getUsername() {
		return this.username;
	}

	public int getId() {
		return this.id;
	}

	public void setID(int id) {
		if (id < -1) throw new IllegalArgumentException();
		this.id = id;
	}

	public Player getPlayer() {
		return this.player;
	}

	public TimerTask getPingTimerTask() {
		return this.task;
	}

	public void setPingTimerTask(TimerTask task) {
		this.task = task;
		timer.schedule(task, TIMEOUT_DURATION);
	}

	public ClientConnection getConnection() {
		return this.connection;
	}

}
