package it.polimi.ingsw.model.state;

import it.polimi.ingsw.controller.server.ClientDescriptor;
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
	 * Validates and applies logic of a {@link it.polimi.ingsw.message.server.ServerMessage}, transitioning the {{@link it.polimi.ingsw.model.state.GameState} if certain conditions are satisfied.
	 * 
	 * @param message {@link it.polimi.ingsw.message.server.ServerMessage} Message to validate.
	 * @throws ForbiddenCallException if the model refuses the message in any sort of way.
	 */
	public abstract void validate(ServerMessage message) throws ForbiddenCallException;

	/**
	 * Returns the state following the current one according to its internal logic, returns null if the game is finished.
	 * 
	 * @return {{@link it.polimi.ingsw.model.state.GameState} Next state to play.
	 */
	public abstract GameState getNext();

	/**
	 * Returns client side representation of the {{@link it.polimi.ingsw.model.state.GameState}.
	 * @return {@link ClientState} Client side representation of the {{@link it.polimi.ingsw.model.state.GameState}.
	 */
	public abstract ClientState getClientState();

	/**
	 * @return Whether the current {{@link it.polimi.ingsw.model.state.GameState} allows the serialization of it.
	 */
	public abstract boolean toSerialize();

	public abstract String toString();

	/**
     * Generates and returns a GameListEntry object containing the lobby details.
     * 
	 * @param id ID of the {@link it.polimi.ingsw.controller.server.LobbyController} managing the overlaying {@link it.polimi.ingsw.model.ModelInstance}.
	 * @return {@link it.polimi.ingsw.model.client.ClientGameListEntry} Entry containing the lobby info.
	 */
	public abstract ClientGameListEntry getOngoingEntry(int id);

	/**
	 * Apply any logic to be applied at the very start of the {{@link it.polimi.ingsw.model.state.GameState}.
	 */
	abstract public void init();

	/**
	 * Request to the {@link it.polimi.ingsw.model.ModelInstance} that owns this {{@link it.polimi.ingsw.model.state.GameState} to broadcast a {@link ClientMessage} to all connected listeners.
	 * 
	 * @param message {@link ClientMessage} Message to be broadcast.
	 */
	public void broadcastMessage(ClientMessage message) {
		this.model.broadcast(message);
	}

	/**
	 * Transition the current state to its own {{@link it.polimi.ingsw.model.state.GameState#getNext()}.
	 */
	public void transition() {
		this.model.setState(this.getNext());
	}

	public PlayerCount getCount() {
		return this.count;
	}

	public GameModeType getType() {
		return this.type;
	}

	public int getModelID() {
		return this.model.getID();
	}

	public ArrayList<Player> getPlayers(){
		return this.players;
	}

	/**
	 * Connects a {@link it.polimi.ingsw.controller.server.ClientDescriptor} to the {{@link it.polimi.ingsw.model.state.GameState}.
	 * 
	 * @param client {@link it.polimi.ingsw.controller.server.ClientDescriptor} Client connecting to the {{@link it.polimi.ingsw.model.state.GameState}.
	 * @throws ForbiddenCallException if the {{@link it.polimi.ingsw.model.state.GameState} refuses the connection
	 */
	public void connect(ClientDescriptor client) throws ForbiddenCallException {
		this.broadcastMessage(new ViewMessage("Client: '" + client.getUsername() + "' tried to disconnect in a state that doesn't allow it!"));
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Client: '" + client.getUsername() + "' tried to connect in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}
	/**
	 * Disconnects a {@link it.polimi.ingsw.controller.server.ClientDescriptor} from the {{@link it.polimi.ingsw.model.state.GameState}.
	 * 
	 * @param client {@link it.polimi.ingsw.controller.server.ClientDescriptor} Client disconnecting from the {{@link it.polimi.ingsw.model.state.GameState}.
	 * @throws ForbiddenCallException if the {{@link it.polimi.ingsw.model.state.GameState} refuses the disconnection
	 */
	public void disconnect(ClientDescriptor client) throws ForbiddenCallException {
		this.broadcastMessage(new ViewMessage("Client: '" + client.getUsername() + "' tried to disconnect in a state that doesn't allow it!"));
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Client: '" + client.getUsername() + "' tried to disconnect in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}
	/**
	 * Connects a {@link it.polimi.ingsw.model.player.Player} to the {{@link it.polimi.ingsw.model.state.GameState}.
	 * 
	 * @param p {@link it.polimi.ingsw.model.player.Player} Player connecting to the {{@link it.polimi.ingsw.model.state.GameState}.
	 * @throws ForbiddenCallException if the {{@link it.polimi.ingsw.model.state.GameState} refuses the connection.
	 */
	public void connect(Player p) throws ForbiddenCallException {
		this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to connect in a state that doesn't allow it!"));
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' tried to connect in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}
	/**
	 * Disconnects a {@link it.polimi.ingsw.model.player.Player} from the {{@link it.polimi.ingsw.model.state.GameState}.
	 * 
	 * @param p {@link it.polimi.ingsw.model.player.Player} Player disconnecting from the {{@link it.polimi.ingsw.model.state.GameState}.
	 * @throws ForbiddenCallException if the {{@link it.polimi.ingsw.model.state.GameState} refuses the disconnection.
	 */
	public void disconnect(Player p) throws ForbiddenCallException {
		this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to disconnect in a state that doesn't allow it!"));
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' tried to disconnect in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}
	/**
	 * Inputs the request by a {@link it.polimi.ingsw.model.player.Player} to progress the game.
	 * 
	 * @param p {@link it.polimi.ingsw.model.player.Player} Player requesting to progress.
	 * @throws ForbiddenCallException if the {{@link it.polimi.ingsw.model.state.GameState} refuses the request.
	 */
	public void sendContinue(Player p) throws ForbiddenCallException {
		this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to send a continue in a state that doesn't allow it!"));
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' tried to send a continue in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}
	/**
	 * Inputs the request by a {@link it.polimi.ingsw.model.player.Player} to place a {@link it.polimi.ingsw.model.components.BaseComponent}  at a {@link it.polimi.ingsw.model.player.ShipCoords location} with a {@link ComponentRotation rotation}.
	 * 
	 * @param p {@link it.polimi.ingsw.model.player.Player} Player requesting to place.
	 * @param id ID of the component being placed.
	 * @param coords {@link it.polimi.ingsw.model.player.ShipCoords} Coords where the {@link it.polimi.ingsw.model.components.BaseComponent}  is being placed.
	 * @param rotation {@link ComponentRotation} Rotation of the component being placed.
	 * @throws ForbiddenCallException if the {{@link it.polimi.ingsw.model.state.GameState} refuses the request.
	 */
	public void putComponent(Player p, int id, ShipCoords coords, ComponentRotation rotation) throws ForbiddenCallException {
		this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to put a component in a state that doesn't allow it!"));
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' tried to put a component in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}
	/**
	 * Inputs the request by a {@link it.polimi.ingsw.model.player.Player} to take a covered {@link it.polimi.ingsw.model.components.BaseComponent}  from the {@link it.polimi.ingsw.model.board.iCommonBoard board}.
	 * 
	 * @param p {@link it.polimi.ingsw.model.player.Player} Player requesting to take.
	 * @throws ForbiddenCallException if the {{@link it.polimi.ingsw.model.state.GameState} refuses the request.
	 */
	public void takeComponent(Player p) throws ForbiddenCallException {
		this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to pick a component in a state that doesn't allow it!"));
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' tried to pick a component in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}
	/**
	 * Inputs the request by a {@link it.polimi.ingsw.model.player.Player} to take a discarded {@link it.polimi.ingsw.model.components.BaseComponent}  from the {@link it.polimi.ingsw.model.board.iCommonBoard board}.
	 * 
	 * @param p {@link it.polimi.ingsw.model.player.Player} Player requesting to take.
	 * @param id ID of the component being taken.
	 * @throws ForbiddenCallException if the {{@link it.polimi.ingsw.model.state.GameState} refuses the request.
	 */
	public void takeDiscarded(Player p, int id) throws ForbiddenCallException {
		this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to take a discarded component in a state that doesn't allow it!"));
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' tried to take a discarded component in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}
	/**
	 * Inputs the request by a {@link it.polimi.ingsw.model.player.Player} to reserve their current {@link it.polimi.ingsw.model.components.BaseComponent} .
	 * 
	 * @param p {@link it.polimi.ingsw.model.player.Player} Player requesting to reserve.
	 * @throws ForbiddenCallException if the {{@link it.polimi.ingsw.model.state.GameState} refuses the request.
	 */
	public void reserveComponent(Player p) throws ForbiddenCallException {
		this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to discard a component in a state that doesn't allow it!"));
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' tried to discard a component in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}
	/**
	 * Inputs the request by a {@link it.polimi.ingsw.model.player.Player} to discard their current {@link it.polimi.ingsw.model.components.BaseComponent} .
	 * 
	 * @param p {@link it.polimi.ingsw.model.player.Player} Player requesting to discard.
	 * @throws ForbiddenCallException if the {{@link it.polimi.ingsw.model.state.GameState} refuses the request.
	 */
	public void discardComponent(Player p) throws ForbiddenCallException {
		this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to reserve a component in a state that doesn't allow it!"));
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' tried to reserve a component in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}
	/**
	 * Inputs the request by a {@link it.polimi.ingsw.model.player.Player} to toggle the {@link it.polimi.ingsw.model.state.ConstructionStateHourglass},
	 * 
	 * @param p {@link it.polimi.ingsw.model.player.Player} Player requesting to toggle.
	 * @throws ForbiddenCallException if the {{@link it.polimi.ingsw.model.state.GameState} refuses the request.
	 */
	public void toggleHourglass(Player p) throws ForbiddenCallException {
		this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to toggle the hourglass in a state that doesn't allow it!"));
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' tried to toggle the hourglass in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}
	/**
	 * Inputs the request by a {@link it.polimi.ingsw.model.player.Player} to remove a {@link it.polimi.ingsw.model.components.BaseComponent} at a {@link it.polimi.ingsw.model.player.ShipCoords location}.
	 * 
	 * @param p {@link it.polimi.ingsw.model.player.Player} Player requesting to remove.
	 * @param coords {@link it.polimi.ingsw.model.player.ShipCoords} Coords where the {@link it.polimi.ingsw.model.components.BaseComponent}  is being removed.
	 * @throws ForbiddenCallException if the {{@link it.polimi.ingsw.model.state.GameState} refuses the request.
	 */
	public void removeComponent(Player p, ShipCoords coords) throws ForbiddenCallException {
		this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to remove a component in a state that doesn't allow it!"));
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' tried to remove a component in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}
	/**
	 * Inputs the request by a {@link it.polimi.ingsw.model.player.Player} to set the {@link it.polimi.ingsw.model.components.enums.AlienType type} of the crew of a {@link it.polimi.ingsw.model.components.CabinComponent} at a {@link it.polimi.ingsw.model.player.ShipCoords location}.
	 * 
	 * @param p {@link it.polimi.ingsw.model.player.Player} Player requesting to set the crew.
	 * @param coords {@link it.polimi.ingsw.model.player.ShipCoords} Coords where the {@link it.polimi.ingsw.model.components.enums.AlienType} is being set.
	 * @param type {@link it.polimi.ingsw.model.components.enums.AlienType} Type of crew to be set.
	 * @throws ForbiddenCallException if the {@link it.polimi.ingsw.model.state.GameState} refuses the request.
	 */
	public void setCrewType(Player p, ShipCoords coords, AlienType type) throws ForbiddenCallException {
		this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to set the crew type in a state that doesn't allow it!"));
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' tried to set the crew type in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}
	/**
	 * Inputs the request by a {@link it.polimi.ingsw.model.player.Player} to give up.
	 * 
	 * @param p {@link it.polimi.ingsw.model.player.Player} Player requesting to give up.
	 * @throws ForbiddenCallException if the {{@link it.polimi.ingsw.model.state.GameState} refuses the request.
	 */
	public void giveUp(Player p) throws ForbiddenCallException {
		this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to give up in a state that doesn't allow it!"));
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' tried to give up in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}

	/**
	 * Inputs the request by a {@link it.polimi.ingsw.model.player.Player} to select a {@link it.polimi.ingsw.model.player.SpaceShip} blob.
	 * 
	 * @param p {@link it.polimi.ingsw.model.player.Player} Player requesting to select a blob.
	 * @param blob_coord {@link it.polimi.ingsw.model.player.ShipCoords} Coords of the selected {@link it.polimi.ingsw.model.player.SpaceShip} blob. 
	 * @throws ForbiddenCallException if the {{@link it.polimi.ingsw.model.state.GameState} refuses the request.
	 */
	public void selectBlob(Player p, ShipCoords blob_coord) throws ForbiddenCallException {
		this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to select a new blob in a state that doesn't allow it!"));
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' tried to select a new blob in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}

	/**
	 * Requests on behalf of a {@link it.polimi.ingsw.message.server.ServerMessage} the {@link CardState} if available.
	 * @param p {@link it.polimi.ingsw.model.player.Player} Player sending the {@link it.polimi.ingsw.message.server.ServerMessage} that requested.
	 * @return {@link CardState} Current card state.
	 * @throws ForbiddenCallException if the state doesn't have a {@link CardState}.
	 */
	public CardState getCardState(Player p) throws ForbiddenCallException {
		this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to get the card state in a state that doesn't allow it!"));
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' tried to get the card state in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}

}
