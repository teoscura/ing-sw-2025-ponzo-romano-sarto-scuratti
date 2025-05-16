package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.DummyController;
import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.message.client.ClientMessage;
import it.polimi.ingsw.message.server.ServerConnectMessage;
import it.polimi.ingsw.message.server.ServerDisconnectMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.state.GameState;
import it.polimi.ingsw.model.state.ResumeWaitingState;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.LoggerLevel;

public class DummyModelInstance extends ModelInstance {

	public DummyModelInstance(int id, GameModeType type, PlayerCount count) {
		super(id, new DummyController(id), type, count);
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
	}

	public void startGame() {
		Logger.getInstance().print(LoggerLevel.MODEL, "["+this.id+"] "+"Game Started. Dummy");
		this.started = true;
	}

	public boolean hasStarted() {
		return this.started;
	}

	public void endGame() {
		if (!this.started) throw new RuntimeException();
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
			Logger.getInstance().print(LoggerLevel.MODEL, "["+this.id+"] "+"Client: '" + client.getUsername() + "' tried connecting when the current state doesn't support it anymore!");
		}
	}

	public void disconnect(ClientDescriptor client) {
		try {
			ServerMessage mess = new ServerDisconnectMessage();
			mess.setDescriptor(client);
			this.state.validate(mess);
		} catch (ForbiddenCallException e) {
			Logger.getInstance().print(LoggerLevel.MODEL, "["+this.id+"] "+"Client: '" + client.getUsername() + "' tried disconnecting when the current state doesn't support it anymore!");
		}
	}

	public void connect(Player p) {
		try {
			this.state.connect(p);
		} catch (ForbiddenCallException e) {
			Logger.getInstance().print(LoggerLevel.MODEL, "["+this.id+"] "+"Client: '" + p.getUsername() + "' tried reconnecting when the current state doesn't support it anymore!");
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
			Logger.getInstance().print(LoggerLevel.MODEL, "["+this.id+"] "+"Client: '" + p.getUsername() + "' tried disconnecting when the current state doesn't support it anymore!");
		}
	}

	public void afterSerialRestart() {
		ResumeWaitingState next = new ResumeWaitingState(this, this.state.getType(), this.state.getCount(), this.state);
		this.setState(next);
	}

	public void broadcast(ClientMessage message) {
	}


}
