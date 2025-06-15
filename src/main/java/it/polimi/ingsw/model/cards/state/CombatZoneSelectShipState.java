package it.polimi.ingsw.model.cards.state;

import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.cards.CombatZoneCard;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.cards.utils.CombatZoneSection;
import it.polimi.ingsw.model.cards.utils.ProjectileArray;
import it.polimi.ingsw.model.client.card.ClientBaseCardState;
import it.polimi.ingsw.model.client.card.ClientCardState;
import it.polimi.ingsw.model.client.card.ClientCombatZoneIndexCardStateDecorator;
import it.polimi.ingsw.model.client.card.ClientNewCenterCardStateDecorator;
import it.polimi.ingsw.model.client.state.ClientState;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.VoyageState;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.LoggerLevel;

import java.util.ArrayList;
import java.util.List;
/**
 * Class representing a Select Ship State of the {@link CombatZoneCard}.
 */
public class CombatZoneSelectShipState extends CardState {

	private final int card_id;
	private final ArrayList<CombatZoneSection> sections;
	private final ProjectileArray shots;
	private final Player target;

	/**
	 * Constructs a new {@code CombatZoneSelectShipState}.
	 *
	 * @param state {@link VoyageState} The current voyage state
	 * @param card_id   The card id
	 * @param sections  The sections defining penalties
	 * @param shots     The projectile array
	 * @param target   The {@link Player} receiving the penalty
	 */
	public CombatZoneSelectShipState(VoyageState state, int card_id, ArrayList<CombatZoneSection> sections, ProjectileArray shots, Player target) {
		super(state);
		if (sections == null || shots == null || target == null) ;
		if (card_id < 1 || card_id > 120) throw new IllegalArgumentException();
		this.card_id = card_id;
		this.sections = sections;
		this.shots = shots;
		this.target = target;
	}

	/**
	 * Called when the card state is initialized.
	 * Resets power for all players ships.
	 *
	 * @param new_state {@link ClientState} The new client state to broadcast to all connected listeners.
	 */
	@Override
	public void init(ClientState new_state) {
		super.init(new_state);
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "CardState -> Combat Zone Select Ship State!: [" + (3 - sections.size()) + " - " + this.sections.getFirst().getPenalty() + "].");
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Awaiting: '" + this.target.getUsername() + "'.");

	}

	/**
	 * Validates the {@link ServerMessage} and transitions if the player has set the blob or disconnected.
	 *
	 * @param message {@link ServerMessage} The message received from the player
	 * @throws ForbiddenCallException if the message is not allowed
	 */
	@Override
	public void validate(ServerMessage message) throws ForbiddenCallException {
		message.receive(this);
		if (target.getSpaceShip().getBlobsSize() > 1 && !target.getDisconnected()) {
			this.state.broadcastMessage(new NotifyStateUpdateMessage(this.state.getClientState()));
			return;
		}
		this.transition();
	}

	@Override
	public ClientCardState getClientCardState() {
		return new ClientNewCenterCardStateDecorator(
				new ClientCombatZoneIndexCardStateDecorator(
						new ClientBaseCardState(
								this.getClass().getSimpleName(),
								this.card_id),
						this.sections.getFirst(),
						3 - this.sections.size()),
				new ArrayList<>(List.of(target.getColor())));
	}

	/**
	 * Computes and returns the next {@code CardState}.
	 *
	 * @return {@link CardState} the next state, or {@code null} if the card is exhausted
	 */
	@Override
	public CardState getNext() {
		if (this.target.getRetired()) {
			this.sections.removeFirst();
			if (!this.sections.isEmpty()) return new CombatZoneAnnounceState(state, card_id, sections, shots);
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Card exhausted, moving to a new one!");
			return null;
		}
		if (!this.shots.getProjectiles().isEmpty())
			return new CombatZonePenaltyState(state, card_id, sections, shots, target);
		this.sections.removeFirst();
		if (!this.sections.isEmpty()) return new CombatZoneAnnounceState(state, card_id, sections, shots);
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
		if (!p.equals(target)) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to set a new center during another player's turn!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to set a new center during another player's turn!"));
			return;
		}
		try {
			p.getSpaceShip().selectShipBlob(blob_coord);
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' selected blob that contains coords " + blob_coord + ".");
		} catch (IllegalTargetException e) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to set his new center on an empty space!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to set his new center on an empty space!"));
		} catch (ForbiddenCallException e) {
			//Should be unreachable.
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to set his new center while having a unbroken ship!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to set his new center while having a unbroken ship!"));
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' set a new ship center.");
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
