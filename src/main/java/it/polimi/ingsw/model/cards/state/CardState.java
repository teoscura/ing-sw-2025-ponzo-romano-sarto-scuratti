package it.polimi.ingsw.model.cards.state;

import it.polimi.ingsw.controller.client.ClientController;
import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.cards.utils.CardOrder;
import it.polimi.ingsw.model.client.card.ClientCardState;
import it.polimi.ingsw.model.client.state.ClientState;
import it.polimi.ingsw.model.components.enums.ShipmentType;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.VoyageState;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.LoggerLevel;
import it.polimi.ingsw.model.cards.Card;

import java.io.Serializable;
/**
 * Abstract class representing a State of the {@link Card}.
 */
public abstract class CardState implements Serializable {

	protected final VoyageState state;

	/**
	 * Construct the abstract base of a {@link CardState} derived object.
	 *
	 * @param state {@link VoyageState} The current voyage state
	 * @throws NullPointerException if {@code state} is null
	 */
	protected CardState(VoyageState state) {
		if (state == null) throw new NullPointerException();
		this.state = state;
	}

	/**
	 * Called when the card state is initialized.
	 * Resets power for all players ships.
	 *
	 * @param new_state {@link ClientController} the new client state to broadcast
	 */
	public void init(ClientState new_state) {
		for (Player p : state.getOrder(CardOrder.NORMAL)) {
			p.getSpaceShip().resetPower();
		}
		this.state.broadcastMessage(new NotifyStateUpdateMessage(new_state));
	}

	public abstract void validate(ServerMessage message) throws ForbiddenCallException;

	public abstract ClientCardState getClientCardState();

	public abstract CardState getNext();

	public void transition() {
		this.state.setCardState(this.getNext());
	}

	/**
	 * This state doesn't allow this action
	 *
	 * @param p {@link Player} The player
	 * @param blob_coord {@link ShipCoords} The coordinates selected
	 * @throws ForbiddenCallException when the state refuses the action.
	 */
	public void selectBlob(Player p, ShipCoords blob_coord) throws ForbiddenCallException {
		this.state.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to set a new center in a state that doesn't allow it!"));
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' tried to connect in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}

	/**
	 * This state doesn't allow this action
	 *
	 * @param p {@link Player} The player
	 * @param target_coords {@link ShipCoords} the component to power
	 * @param battery_coords {@link ShipCoords} the battery to use
	 * @throws ForbiddenCallException when the state refuses the action.
	 */
	public void turnOn(Player p, ShipCoords target_coords, ShipCoords battery_coords) throws ForbiddenCallException {
		this.state.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to set a new center in a state that doesn't allow it!"));
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' tried to connect in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}

	/**
	 * This state doesn't allow this action
	 *
	 * @param p {@link Player} The player
	 * @param cabin_coords {@link ShipCoords} the coordinates of the cabin
	 * @throws ForbiddenCallException when the state refuses the action.
	 */
	public void removeCrew(Player p, ShipCoords cabin_coords) throws ForbiddenCallException {
		this.state.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to remove crew in a state that doesn't allow it!"));
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' tried to connect in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}

	/**
	 * This state doesn't allow this action
	 *
	 * @param p {@link Player} The player
	 * @param shipment {@link ShipmentType} The cargo type
	 * @param target_coords {@link ShipCoords} the coordinates of the target
	 * @param source_coords {@link ShipCoords} the coordinates of the source
	 * @throws ForbiddenCallException when the state refuses the action.
	 */
	public void moveCargo(Player p, ShipmentType shipment, ShipCoords target_coords, ShipCoords source_coords) throws ForbiddenCallException {
		this.state.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to remove crew in a state that doesn't allow it!"));
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' tried to connect in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}

	/**
	 * This state doesn't allow this action
	 *
	 * @param p {@link Player} The player
	 * @param type {@link ShipmentType} The cargo type
	 * @param storage_coords {@link ShipCoords} the coordinates of the storage
	 * @throws ForbiddenCallException when the state refuses the action.
	 */
	public void takeCargo(Player p, ShipmentType type, ShipCoords storage_coords) throws ForbiddenCallException {
		this.state.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to take cargo in a state that doesn't allow it!"));
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' tried to take cargo in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}

	/**
	 * This state doesn't allow this action
	 *
	 * @param p {@link Player} The player
	 * @param type {@link ShipmentType} The cargo type
	 * @param target_coords {@link ShipCoords} the coordinates of the target
	 * @throws ForbiddenCallException when the state refuses the action.
	 */
	public void discardCargo(Player p, ShipmentType type, ShipCoords target_coords) throws ForbiddenCallException {
		this.state.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to discard cargo in a state that doesn't allow it!"));
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' tried to discard cargo in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}

	/**
	 * This state doesn't allow this action
	 *
	 * @param p {@link Player} The player
	 * @param planet The selected planet
	 * @throws ForbiddenCallException when the state refuses the action.
	 */
	public void selectLanding(Player p, int planet) throws ForbiddenCallException {
		this.state.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to select landing in a state that doesn't allow it!"));
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' tried to select landing in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}

	/**
	 * This state doesn't allow this action
	 *
	 * @param p {@link Player} The player
	 * @throws ForbiddenCallException when the state refuses the action.
	 */
	public void progressTurn(Player p) throws ForbiddenCallException {
		this.state.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to progress in a state that doesn't allow it!"));
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' tried to progress in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}

	/**
	 * This state doesn't allow this action
	 *
	 * @param p {@link Player} The player
	 * @param take true if the player wants to take the reward
	 * @throws ForbiddenCallException when the state refuses the action.
	 */
	public void setTakeReward(Player p, boolean take) throws ForbiddenCallException {
		this.state.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to take the reward in a state that doesn't allow it!"));
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' tried to take the reward in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}

	/**
	 * This state doesn't allow this action
	 *
	 * @param p {@link Player} The player
	 * @throws ForbiddenCallException when the state refuses the action.
	 */
	public void connect(Player p) throws ForbiddenCallException {
		this.state.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to connect in a state that doesn't allow it!"));
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' tried to connect in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}

	/**
	 * This state doesn't allow this action
	 *
	 * @param p {@link Player} The player
	 * @throws ForbiddenCallException when the state refuses the action.
	 */
	public void disconnect(Player p) throws ForbiddenCallException {
		this.state.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to disconnect in a state that doesn't allow it!"));
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' tried to disconnect in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}

}
