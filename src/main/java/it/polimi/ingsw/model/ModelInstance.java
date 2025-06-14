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
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.LoggerLevel;

import java.io.Serializable;

/**
 * Class represeting an entire instance of a game of Galaxy Trucker.
 */
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

	/**
	 * Receives the {@link ServerMessage} and processes it.
	 * 
	 * @param message {@link ServerMessage} Message received.
	 * @throws ForbiddenCallException if the model refuses the message.
	 */
	public void validate(ServerMessage message) throws ForbiddenCallException {
		message.receive(this);
	}

	/**
	 * Forwards a request to the {@link LobbyController} to serialize the current model state.
	 */
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

	/**
	 * Sets the next {@link GameState}, if {@code next} is null, then the game is over.
	 * @param next {@link GameState} State to set and initialize.
	 */
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

	/**
	 * Connect a {@link ClientDescriptor} to the model.
	 * @param client {@link ClientDescriptor} Client connecting to the model.
	 */
	public void connect(ClientDescriptor client) {
		try {
			ServerMessage mess = new ServerConnectMessage(client);
			this.state.validate(mess);
		} catch (ForbiddenCallException e) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + this.id + "] " + "Client: '" + client.getUsername() + "' tried connecting when the current state doesn't support it anymore!");
		}
	}

	/**
	 * Disconnect a {@link ClientDescriptor} to the model.
	 * @param client {@link ClientDescriptor} Client disconnecting from the model.
	 */
	public void disconnect(ClientDescriptor client) {
		try {
			ServerMessage mess = new ServerDisconnectMessage();
			mess.setDescriptor(client);
			this.state.validate(mess);
		} catch (ForbiddenCallException e) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + this.id + "] " + "Client: '" + client.getUsername() + "' tried disconnecting when the current state doesn't support it anymore!");
		}
	}

	/**
	 * Connect a {@link Player} to the model.
	 * @param p {@link Player} Player connecting to the model.
	 */
	public void connect(Player p) {
		try {
			this.state.connect(p);
		} catch (ForbiddenCallException e) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + this.id + "] " + "Player: '" + p.getUsername() + "' tried reconnecting when the current state doesn't support it!");
		}
	}

	/**
	 * Disconnect a {@link Player} to the model.
	 * @param p {@link Player} Player disconnecting from the model.
	 */
	public void disconnect(Player p) {
		try {
			if (p == null) throw new NullPointerException();
			if (p.getDisconnected()) throw new ForbiddenCallException();
			ServerMessage disc = new ServerDisconnectMessage();
			disc.setDescriptor(p.getDescriptor());
			this.state.validate(disc);
		} catch (ForbiddenCallException e) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + this.id + "] " + "Player: '" + p.getUsername() + "' tried disconnecting when the current state doesn't support it!");
		}
	}

	public LobbyController getController() {
		return this.controller;
	}

	public void setController(LobbyController controller) {
		this.controller = controller;
	}

	/**
	 * Routine to restart game after a crash or closing, creates a {@link ResumeWaitingState} linked to the previous {@link GameState} and sets it as the game state.
	 */
	public void afterSerialRestart() {
		this.started = false;
		ResumeWaitingState next = new ResumeWaitingState(this, this.state.getType(), this.state.getCount(), this.state);
		this.setState(next);
	}

	/**
	 * Requests the {@link LobbyController} to broadcast a {@link ClientMessage} to all connected listeners.
	 * 
	 * @param message {@link ClientMessage} Message to be broadcast.
	 */
	public void broadcast(ClientMessage message) {
		this.controller.broadcast(message);
	}

	/**
	 * @return {@link ClientGameListEntry} An entry containing info about this {@link ModelInstance} to be sent to a client.
	 */
	public ClientGameListEntry getEntry() {
		return this.state.getOngoingEntry(this.id);
	}

	/**
	 * Requests the {@link LobbyController} to end the game and all associated resources.
	 */
	public void endGame() {
		this.controller.endGame();
	}

}
