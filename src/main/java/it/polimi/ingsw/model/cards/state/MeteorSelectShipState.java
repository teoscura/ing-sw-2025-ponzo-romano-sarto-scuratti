package it.polimi.ingsw.model.cards.state;

import it.polimi.ingsw.controller.client.ClientController;
import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.cards.MeteorSwarmCard;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.cards.utils.CardOrder;
import it.polimi.ingsw.model.cards.utils.ProjectileArray;
import it.polimi.ingsw.model.client.card.ClientBaseCardState;
import it.polimi.ingsw.model.client.card.ClientCardState;
import it.polimi.ingsw.model.client.card.ClientNewCenterCardStateDecorator;
import it.polimi.ingsw.model.client.state.ClientState;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.VoyageState;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.LoggerLevel;

import java.util.ArrayList;
import java.util.List;
/**
 * Class representing a Select Ship State of the {@link MeteorSwarmCard}.
 */
class MeteorSelectShipState extends CardState {

	private final int card_id;
	private final ProjectileArray left;

	/**
	 * Constructs a new {@code MeteorSelectShipState}.
	 *
	 * @param state {@link VoyageState} The current voyage state
	 * @param card_id   The card id
	 * @param left     The projectile array
	 */
	public MeteorSelectShipState(VoyageState state, int card_id, ProjectileArray left) {
		super(state);
		if (left == null) throw new NullPointerException();
		if (card_id < 1 || card_id > 120) throw new IllegalArgumentException();
		this.card_id = card_id;
		this.left = left;
	}

	/**
	 * Called when the card state is initialized.
	 * Resets power for all players ships.
	 *
	 * @param new_state {@link ClientController} The new client state to broadcast to all connected listeners.
	 */
	@Override
	public void init(ClientState new_state) {
		super.init(new_state);
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "CardState -> Meteor Swarm Select Ship State!");
	}

	/**
	 *
	 *
	 * @param message {@link ServerMessage} The message received from the player
	 * @throws ForbiddenCallException if the message is not allowed
	 */
	@Override
	public void validate(ServerMessage message) throws ForbiddenCallException {
		message.receive(this);
		boolean missing = false;
		for (Player p : this.state.getOrder(CardOrder.NORMAL)) {
			missing = missing || p.getSpaceShip().getBlobsSize() > 1;
		}
		if (missing) {
			this.state.broadcastMessage(new NotifyStateUpdateMessage(this.state.getClientState()));
			return;
		}
		this.transition();
	}

	@Override
	public ClientCardState getClientCardState() {
		List<PlayerColor> tmp = new ArrayList<>();
		for (Player p : this.state.getOrder(CardOrder.NORMAL)) {
			if (p.getSpaceShip().getBlobsSize() > 1) tmp.add(p.getColor());
		}
		return new ClientNewCenterCardStateDecorator(new ClientBaseCardState(this.getClass().getSimpleName(), card_id), new ArrayList<>(tmp));
	}

	/**
	 * Computes and returns the next {@code CardState}.
	 *
	 * @return the next state, or {@code null} if the card is exhausted
	 */
	@Override
	public CardState getNext() {
		this.left.getProjectiles().removeFirst();
		if (!this.left.getProjectiles().isEmpty()) return new MeteorAnnounceState(state, card_id, left);
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Card exhausted, moving to a new one!");
		return null;
	}

	/**
	 * Called when a {@link Player} tries to select a ship blob center.
	 *
	 * @param p {@link Player} The player
	 * @param blob_coord {@link ShipCoords} The coordinates selected
	 */
	@Override
	public void selectBlob(Player p, ShipCoords blob_coord) {
		try {
			p.getSpaceShip().selectShipBlob(blob_coord);
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' selected blob that contains coords " + blob_coord + ".");
		} catch (IllegalTargetException e) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to set his new center on an empty space!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to set his new center on an empty space!"));
		} catch (ForbiddenCallException e) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to set his new center while having a unbroken ship!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to set his new center while having a unbroken ship!"));
		}
	}

	/**
	 * Called when a {@link Player} disconnects.
	 *
	 * @param p {@link Player} The player disconnecting.
	 * @throws ForbiddenCallException when the state refuses the action.
	 */
	@Override
	public void disconnect(Player p) throws ForbiddenCallException {

	}

}
