package it.polimi.ingsw.model.state;

import java.io.Serializable;
import java.util.ArrayList;

import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.exceptions.PlayerNotFoundException;
import it.polimi.ingsw.message.client.ClientMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.cards.state.CardState;
import it.polimi.ingsw.model.client.state.ClientModelState;
import it.polimi.ingsw.model.components.enums.AlienType;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.ShipCoords;

public abstract class GameState implements Serializable {

	protected final ModelInstance model;
	protected final GameModeType type;
	protected final PlayerCount count;
	protected final ArrayList<Player> players;

	public GameState(ModelInstance model, GameModeType type, PlayerCount count, ArrayList<Player> players) {
		if (model == null) throw new NullPointerException();
		if (players != null && players.size() != count.getNumber())
			throw new IllegalArgumentException("Illegal GameState created");
		this.model = model;
		this.type = type;
		this.count = count;
		this.players = players;
	}

	public abstract void validate(ServerMessage message) throws ForbiddenCallException;

	public abstract GameState getNext();

	public abstract ClientModelState getClientState();

	public abstract boolean toSerialize();

	public abstract String toString();

	public void init() {
	}

	public void broadcastMessage(ClientMessage message) {
		this.model.getController().broadcast(message);
	}

	public void transition() {
		this.model.setState(this.getNext());
	}

	public PlayerCount getCount() {
		return this.count;
	}

	public Player getPlayer(PlayerColor c) throws PlayerNotFoundException {
		if (this.players == null || c.getOrder() >= players.size())
			throw new PlayerNotFoundException("Player color is not present in this match");
		return this.players.get(c.getOrder());
	}

	public GameModeType getType() {
		return this.type;
	}

	//Methods that can be called and overridden by subclasses.
	public void connect(ClientDescriptor client) throws ForbiddenCallException {
		this.broadcastMessage(new ViewMessage("Client: '" + client.getUsername() + "' tried to disconnect in a state that doesn't allow it!"));
		System.out.println("Client: '" + client.getUsername() + "' tried to connect in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}

	public void disconnect(ClientDescriptor client) throws ForbiddenCallException {
		this.broadcastMessage(new ViewMessage("Client: '" + client.getUsername() + "' tried to disconnect in a state that doesn't allow it!"));
		System.out.println("Client: '" + client.getUsername() + "' tried to disconnect in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}

	public void connect(Player p) throws ForbiddenCallException {
		this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to connect in a state that doesn't allow it!"));
		System.out.println("Player: '" + p.getUsername() + "' tried to connect in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}

	public void disconnect(Player p) throws ForbiddenCallException {
		this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to disconnect in a state that doesn't allow it!"));
		System.out.println("Player: '" + p.getUsername() + "' tried to disconnect in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}

	public void sendContinue(Player p) throws ForbiddenCallException {
		this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to send a continue in a state that doesn't allow it!"));
		System.out.println("Player: '" + p.getUsername() + "' tried to send a continue in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}

	public void putComponent(Player p, ShipCoords coords, ComponentRotation rotation) throws ForbiddenCallException {
		this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to put a component in a state that doesn't allow it!"));
		System.out.println("Player: '" + p.getUsername() + "' tried to put a component in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}

	public void takeComponent(Player p) throws ForbiddenCallException {
		this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to pick a component in a state that doesn't allow it!"));
		System.out.println("Player: '" + p.getUsername() + "' tried to pick a component in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}

	public void takeDiscarded(Player p, int id) throws ForbiddenCallException {
		this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to take a discarded component in a state that doesn't allow it!"));
		System.out.println("Player: '" + p.getUsername() + "' tried to take a discarded component in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}

	public void discardComponent(Player p, int id) throws ForbiddenCallException {
		this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to discard a component in a state that doesn't allow it!"));
		System.out.println("Player: '" + p.getUsername() + "' tried to discard a component in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}

	public void toggleHourglass(Player p) throws ForbiddenCallException {
		this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to toggle the hourglass in a state that doesn't allow it!"));
		System.out.println("Player: '" + p.getUsername() + "' tried to toggle the hourglass in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}

	public void removeComponent(Player p, ShipCoords coords) throws ForbiddenCallException {
		this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to remove a component in a state that doesn't allow it!"));
		System.out.println("Player: '" + p.getUsername() + "' tried to remove a component in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}

	public void setCrewType(Player p, ShipCoords coords, AlienType type) throws ForbiddenCallException {
		this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to set the crew type in a state that doesn't allow it!"));
		System.out.println("Player: '" + p.getUsername() + "' tried to set the crew type in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}

	public void giveUp(Player p) throws ForbiddenCallException {
		this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to give up in a state that doesn't allow it!"));
		System.out.println("Player: '" + p.getUsername() + "' tried to give up in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}

	public void selectBlob(Player p, ShipCoords blob_coord) throws ForbiddenCallException {
		this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to select a new blob in a state that doesn't allow it!"));
		System.out.println("Player: '" + p.getUsername() + "' tried to select a new blob in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}

	public CardState getCardState(Player p) throws ForbiddenCallException {
		this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to get the card state in a state that doesn't allow it!"));
		System.out.println("Player: '" + p.getUsername() + "' tried to get the card state in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}

}
