package it.polimi.ingsw.model.state;

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
import it.polimi.ingsw.model.client.ClientGameListEntry;
import it.polimi.ingsw.model.client.state.ClientState;
import it.polimi.ingsw.model.components.enums.AlienType;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.LoggerLevel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Abstract class representing the state of a game of Galaxy Trucker.
 */
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

	/**
	 * Validates and applies logic of a {@link ServerMessage}, transitioning the {@link GameState} if certain conditions are satisfied.
	 * 
	 * @param message {@link ServerMessage} Message to validate.
	 * @throws ForbiddenCallException if the model refuses the message in any sort of way.
	 */
	public abstract void validate(ServerMessage message) throws ForbiddenCallException;

	/**
	 * Returns the state following the current one according to its internal logic, returns null if the game is finished.
	 * 
	 * @return {@link GameState} Next state to play.
	 */
	public abstract GameState getNext();

	/**
	 * Returns client side representation of the {@link GameState}.
	 * @return {@link ClientState} Client side representation of the {@link GameState}.
	 */
	public abstract ClientState getClientState();

	/**
	 * @return Whether the current {@link GameState} allows the serialization of it.
	 */
	public abstract boolean toSerialize();

	public abstract String toString();

	/**
     * Generates and returns a GameListEntry object containing the lobby details.
     * 
	 * @param id ID of the {@link LobbyController} managing the overlaying {@link ModelInstance}.
	 * @return {@link ClientGameListEntry} Entry containing the lobby info.
	 */
	public abstract ClientGameListEntry getOngoingEntry(int id);

	/**
	 * Apply any logic to be applied at the very start of the {@link GameState}.
	 */
	abstract public void init();

	/**
	 * Request to the {@link ModelInstance} that owns this {@link GameState} to broadcast a {@link ClientMessage} to all connected listeners.
	 * 
	 * @param message {@link ClientMessage} Message to be broadcast.
	 */
	public void broadcastMessage(ClientMessage message) {
		this.model.broadcast(message);
	}

	/**
	 * Transition the current state to its own {@link GameState#getNext()}.
	 */
	public void transition() {
		this.model.setState(this.getNext());
	}

	public PlayerCount getCount() {
		return this.count;
	}

	public Player getPlayer(PlayerColor c) throws PlayerNotFoundException {
		if (this.players == null || c.getOrder() >= players.size())
			throw new PlayerNotFoundException("Player: color is not present in this match");
		return this.players.get(c.getOrder());
	}

	public GameModeType getType() {
		return this.type;
	}

	public int getModelID() {
		return this.model.getID();
	}

	/**
	 * Connects a {@link ClientDescriptor} to the {@link GameState}.
	 * 
	 * @param client {@link ClientDescriptor} Client connecting to the {@link GameState}.
	 * @throws ForbiddenCallException if the {@link GameState} refuses the connection
	 */
	public void connect(ClientDescriptor client) throws ForbiddenCallException {
		this.broadcastMessage(new ViewMessage("Client: '" + client.getUsername() + "' tried to disconnect in a state that doesn't allow it!"));
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Client: '" + client.getUsername() + "' tried to connect in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}
	/**
	 * Disconnects a {@link ClientDescriptor} from the {@link GameState}.
	 * 
	 * @param client {@link ClientDescriptor} Client disconnecting from the {@link GameState}.
	 * @throws ForbiddenCallException if the {@link GameState} refuses the disconnection
	 */
	public void disconnect(ClientDescriptor client) throws ForbiddenCallException {
		this.broadcastMessage(new ViewMessage("Client: '" + client.getUsername() + "' tried to disconnect in a state that doesn't allow it!"));
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Client: '" + client.getUsername() + "' tried to disconnect in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}
	/**
	 * Connects a {@link Player} to the {@link GameState}.
	 * 
	 * @param p {@link Player} Player connecting to the {@link GameState}.
	 * @throws ForbiddenCallException if the {@link GameState} refuses the connection.
	 */
	public void connect(Player p) throws ForbiddenCallException {
		this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to connect in a state that doesn't allow it!"));
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' tried to connect in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}
	/**
	 * Disconnects a {@link Player} from the {@link GameState}.
	 * 
	 * @param p {@link Player} Player disconnecting from the {@link GameState}.
	 * @throws ForbiddenCallException if the {@link GameState} refuses the disconnection.
	 */
	public void disconnect(Player p) throws ForbiddenCallException {
		this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to disconnect in a state that doesn't allow it!"));
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' tried to disconnect in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}
	/**
	 * Inputs the request by a {@link Player} to progress the game.
	 * 
	 * @param p {@link Player} Player requesting to progress.
	 * @throws ForbiddenCallException if the {@link GameState} refuses the request.
	 */
	public void sendContinue(Player p) throws ForbiddenCallException {
		this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to send a continue in a state that doesn't allow it!"));
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' tried to send a continue in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}
	/**
	 * Inputs the request by a {@link Player} to place a {@link BaseComponent} at a {@link ShipCoords location} with a {@link ComponentRotation rotation}.
	 * 
	 * @param p {@link Player} Player requesting to place.
	 * @param id ID of the component being placed.
	 * @param coords {@link ShipCoords} Coords where the {@link BaseComponent} is being placed.
	 * @param rotation {@link ComponentRotation} Rotation of the component being placed.
	 * @throws ForbiddenCallException if the {@link GameState} refuses the request.
	 */
	public void putComponent(Player p, int id, ShipCoords coords, ComponentRotation rotation) throws ForbiddenCallException {
		this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to put a component in a state that doesn't allow it!"));
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' tried to put a component in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}
	/**
	 * Inputs the request by a {@link Player} to take a covered {@link BaseComponent} from the {@link iCommonBoard board}.
	 * 
	 * @param p {@link Player} Player requesting to take.
	 * @throws ForbiddenCallException if the {@link GameState} refuses the request.
	 */
	public void takeComponent(Player p) throws ForbiddenCallException {
		this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to pick a component in a state that doesn't allow it!"));
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' tried to pick a component in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}
	/**
	 * Inputs the request by a {@link Player} to take a discarded {@link BaseComponent} from the {@link iCommonBoard board}.
	 * 
	 * @param p {@link Player} Player requesting to take.
	 * @param id ID of the component being taken.
	 * @throws ForbiddenCallException if the {@link GameState} refuses the request.
	 */
	public void takeDiscarded(Player p, int id) throws ForbiddenCallException {
		this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to take a discarded component in a state that doesn't allow it!"));
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' tried to take a discarded component in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}
	/**
	 * Inputs the request by a {@link Player} to reserve their current {@link BaseComponent}.
	 * 
	 * @param p {@link Player} Player requesting to reserve.
	 * @throws ForbiddenCallException if the {@link GameState} refuses the request.
	 */
	public void reserveComponent(Player p) throws ForbiddenCallException {
		this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to discard a component in a state that doesn't allow it!"));
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' tried to discard a component in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}
	/**
	 * Inputs the request by a {@link Player} to discard their current {@link BaseComponent}.
	 * 
	 * @param p {@link Player} Player requesting to discard.
	 * @throws ForbiddenCallException if the {@link GameState} refuses the request.
	 */
	public void discardComponent(Player p) throws ForbiddenCallException {
		this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to reserve a component in a state that doesn't allow it!"));
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' tried to reserve a component in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}
	/**
	 * Inputs the request by a {@link Player} to toggle the {@link ConstructionStateHourglass},
	 * 
	 * @param p {@link Player} Player requesting to toggle.
	 * @throws ForbiddenCallException if the {@link GameState} refuses the request.
	 */
	public void toggleHourglass(Player p) throws ForbiddenCallException {
		this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to toggle the hourglass in a state that doesn't allow it!"));
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' tried to toggle the hourglass in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}
	/**
	 * Inputs the request by a {@link Player} to remove a {@link BaseComponent} at a {@link ShipCoords location}.
	 * 
	 * @param p {@link Player} Player requesting to remove.
	 * @param coords {@link ShipCoords} Coords where the {@link BaseComponent} is being removed.
	 * @throws ForbiddenCallException if the {@link GameState} refuses the request.
	 */
	public void removeComponent(Player p, ShipCoords coords) throws ForbiddenCallException {
		this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to remove a component in a state that doesn't allow it!"));
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' tried to remove a component in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}
	/**
	 * Inputs the request by a {@link Player} to set the {@link AlientType type} of the crew of a {@link CabinComponent} at a {@link ShipCoords location}.
	 * 
	 * @param p {@link Player} Player requesting to set the crew.
	 * @param coords {@link ShipCoords} Coords where the {@link AlienType} is being set.
	 * @param type {@link AlienType} Type of crew to be set.
	 * @throws ForbiddenCallException if the {@link GameState} refuses the request.
	 */
	public void setCrewType(Player p, ShipCoords coords, AlienType type) throws ForbiddenCallException {
		this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to set the crew type in a state that doesn't allow it!"));
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' tried to set the crew type in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}
	/**
	 * Inputs the request by a {@link Player} to give up.
	 * 
	 * @param p {@link Player} Player requesting to give up.
	 * @throws ForbiddenCallException if the {@link GameState} refuses the request.
	 */
	public void giveUp(Player p) throws ForbiddenCallException {
		this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to give up in a state that doesn't allow it!"));
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' tried to give up in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}

	/**
	 * Inputs the request by a {@link Player} to select a {@link SpaceShip} blob.
	 * 
	 * @param p {@link Player} Player requesting to select a blob.
	 * @param coords {@link ShipCoords} Coords of the selected {@link SpaceShip} blob. 
	 * @throws ForbiddenCallException if the {@link GameState} refuses the request.
	 */
	public void selectBlob(Player p, ShipCoords blob_coord) throws ForbiddenCallException {
		this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to select a new blob in a state that doesn't allow it!"));
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' tried to select a new blob in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}

	/**
	 * Requests on behalf of a {@link ServerMessage} the {@link CardState} if available.
	 * @param p {@link Player} Player sending the {@link ServerMessage} that requested.
	 * @return {@link CardState} Current card state.
	 * @throws ForbiddenCallException if the state doesn't have a {@link CardState}.
	 */
	public CardState getCardState(Player p) throws ForbiddenCallException {
		this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to get the card state in a state that doesn't allow it!"));
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' tried to get the card state in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}

}
