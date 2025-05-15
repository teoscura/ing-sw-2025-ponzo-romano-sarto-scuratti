package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.controller.server.LobbyController;
import it.polimi.ingsw.message.client.ClientMessage;
import it.polimi.ingsw.message.server.ServerConnectMessage;
import it.polimi.ingsw.message.server.ServerDisconnectMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.client.ClientGameListEntry;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.state.GameState;
import it.polimi.ingsw.model.state.ResumeWaitingState;
import it.polimi.ingsw.model.state.WaitingState;

import java.io.Serializable;

public class ModelInstance implements Serializable {

	protected final int id;
	protected transient LobbyController controller;
	protected boolean started;
	protected boolean ended;
	protected GameState state;

	public ModelInstance(int id, LobbyController controller, GameModeType type, PlayerCount count) {
		if (id < 0) throw new IllegalArgumentException();
		this.id = id;
		this.state = new WaitingState(this, type, count);
		this.controller = controller;
		this.state.init();
	}

	public int getID() {
		return this.id;
	}

	public void validate(ServerMessage message) throws ForbiddenCallException {
		message.receive(this);
	}

	public void serialize() {
		if (!this.state.toSerialize()) return;
		this.controller.serializeCurrentGame();
	}

	public void startGame() {
		this.started = true;
	}

	public boolean getStarted() {
		return this.started;
	}

	public GameState getState() {
		return this.state;
	}

	public void setState(GameState next) {
		this.state = next;
		if (this.state == null) {
			return;
		}
		if (this.state.toSerialize()) {
			this.serialize();
		}
		next.init();
	}

	public void resumeState(GameState next) {
		this.state = next;
		if (this.state == null) {
			return;
		}
		if (this.state.toSerialize()) {
			this.serialize();
		}
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

	public LobbyController getController() {
		return this.controller;
	}

	public void setController(LobbyController controller) {
		this.controller = controller;
	}

	public void afterSerialRestart() {
		this.started = false;
		ResumeWaitingState next = new ResumeWaitingState(this, this.state.getType(), this.state.getCount(), this.state);
		this.setState(next);
	}

	public void broadcast(ClientMessage message) {
		this.controller.broadcast(message);
	}

	public ClientGameListEntry getEntry() {
		return this.state.getOngoingEntry(this.id);
	}

	public void endGame() {
		this.controller.endGame();
	}

}
