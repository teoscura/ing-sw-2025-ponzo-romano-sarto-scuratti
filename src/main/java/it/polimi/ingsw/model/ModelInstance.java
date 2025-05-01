package it.polimi.ingsw.model;

import java.util.List;

import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.controller.server.ServerController;
import it.polimi.ingsw.message.server.ServerConnectMessage;
import it.polimi.ingsw.message.server.ServerDisconnectMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.player.*;
import it.polimi.ingsw.model.state.GameState;
import it.polimi.ingsw.model.state.ResumeWaitingState;
import it.polimi.ingsw.model.state.WaitingState;

public class ModelInstance {

	private final int id;
	private transient ServerController controller;
	private boolean started;
	private GameState state;

	public ModelInstance(int id, ServerController server, GameModeType type, PlayerCount count) {
		if (id < 0) throw new IllegalArgumentException();
		this.id = id;
		this.controller = server;
		this.state = new WaitingState(this, type, count);
		this.state.init();
	}

	public String toString() {
		return id + " - " + this.state.toString();
	}

	public int getID() {
		return this.id;
	}

	public void validate(ServerMessage message) throws ForbiddenCallException {
		message.receive(this);
	}

	public void serialize() {
		this.controller.serializeCurrentGame();
	}

	public void startGame(List<Player> players) throws ForbiddenCallException {
		if (this.started) throw new ForbiddenCallException();
		this.started = true;
	}

	public boolean getStarted() {
		return this.started;
	}

	public void endGame() {
		if (!this.started) throw new RuntimeException();
		this.controller.endGame();
	}

	public GameState getState() {
		return this.state;
	}

	public void setState(GameState next) {
		if (next == null) {
			this.endGame();
		}
		this.state = next;
		if (this.state.toSerialize()) {
			this.serialize();
		}
		next.init();
	}

	public void connect(ClientDescriptor client) {
		try {
			ServerMessage mess = new ServerConnectMessage(client);
			this.state.validate(mess);
		} catch (ForbiddenCallException e) {
			System.out.println("Client: '" + client.getUsername() + "' tried connecting when the current state doesn't support it anymore!");
		}
	}

	public void disconnect(ClientDescriptor client) {
		try {
			ServerMessage mess = new ServerDisconnectMessage();
			mess.setDescriptor(client);
			this.state.validate(mess);
		} catch (ForbiddenCallException e) {
			System.out.println("Client: '" + client.getUsername() + "' tried disconnecting when the current state doesn't support it anymore!");
		}
	}

	public void connect(Player p) {
		try {
			this.state.connect(p);
		} catch (ForbiddenCallException e) {
			System.out.println("Client: '" + p.getUsername() + "' tried reconnecting when the current state doesn't support it anymore!");
		}
	}

	public void disconnect(Player p) {
		try {
			if (p == null) throw new NullPointerException();
			if (p.getDisconnected()) throw new ForbiddenCallException();
			ServerMessage disc = new ServerDisconnectMessage();
			disc.setDescriptor(p.getDescriptor());
			this.state.validate(disc);
		} catch (ForbiddenCallException e) {
			System.out.println("Client: '" + p.getUsername() + "' tried disconnecting when the current state doesn't support it anymore!");
		}
	}

	public void setController(ServerController controller) {
		this.controller = controller;
	}

	public ServerController getController() {
		return this.controller;
	}

	public void afterSerialRestart() {
		ResumeWaitingState next = new ResumeWaitingState(this, this.state.getType(), this.state.getCount(), this.state);
		this.setState(next);
	}

}
