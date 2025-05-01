package it.polimi.ingsw.model;

import java.util.List;

import it.polimi.ingsw.controller.DummyController;
import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.controller.server.ServerController;
import it.polimi.ingsw.message.server.ServerConnectMessage;
import it.polimi.ingsw.message.server.ServerDisconnectMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.player.*;
import it.polimi.ingsw.model.state.GameState;
import it.polimi.ingsw.model.state.ResumeWaitingState;

public class DummyModelInstance extends ModelInstance {

	private final int id;
	private boolean started;
	private GameState state;

	public DummyModelInstance(int id, ServerController server, GameModeType type, PlayerCount count) {
		super(id, server, type, count);
		if (id < 0) throw new IllegalArgumentException();
		this.id = id;
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
		return;
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
		return;
	}

	public GameState getState() {
		return this.state;
	}

	public void setState(GameState next) {
		if (next == null) {
			this.endGame();
		}
		this.state = next;
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

	public ServerController getController() {
		return new DummyController();
	}

	public void afterSerialRestart() {
		ResumeWaitingState next = new ResumeWaitingState(this, this.state.getType(), this.state.getCount(), this.state);
		this.setState(next);
	}

}
