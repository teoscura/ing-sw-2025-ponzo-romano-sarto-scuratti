package it.polimi.ingsw.model.cards.state;

import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.cards.CombatZoneCard;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.cards.utils.CardOrder;
import it.polimi.ingsw.model.cards.utils.CombatZoneSection;
import it.polimi.ingsw.model.cards.utils.ProjectileArray;
import it.polimi.ingsw.model.client.card.ClientAwaitConfirmCardStateDecorator;
import it.polimi.ingsw.model.client.card.ClientBaseCardState;
import it.polimi.ingsw.model.client.card.ClientCardState;
import it.polimi.ingsw.model.client.card.ClientCombatZoneIndexCardStateDecorator;
import it.polimi.ingsw.model.client.state.ClientState;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.VoyageState;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.LoggerLevel;

import java.util.ArrayList;
/**
 * Class representing an Announce State of the {@link CombatZoneCard}.
 */
public class CombatZoneAnnounceState extends CardState {

	private final int card_id;
	private final ArrayList<CombatZoneSection> sections;
	private final ProjectileArray shots;
	private final ArrayList<Player> awaiting;
	private Player target;

	/**
	 * Constructs a new {@code AbandonedStationRewardState}.
	 *
	 * @param state {@link it.polimi.ingsw.model.state.VoyageState} The current voyage state
	 * @param card_id   The card id
	 * @param sections  the sections defining penalties
	 * @param shots     the projectile array
	 */
	public CombatZoneAnnounceState(VoyageState state, int card_id, ArrayList<CombatZoneSection> sections, ProjectileArray shots) {
		super(state);
		if (sections == null || shots == null) throw new NullPointerException();
		if (card_id < 1 || card_id > 120) throw new IllegalArgumentException();
		this.card_id = card_id;
		this.sections = sections;
		this.shots = shots;
		this.awaiting = new ArrayList<>(this.state.getOrder(CardOrder.NORMAL));
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
		if (this.state.getOrder(CardOrder.NORMAL).size() <= 1) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Only one player left playing, skipping state!");
			this.transition();
			return;
		}
		if (sections.size() == 3)
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "New CardState -> Combat Zone Announce State! [Section " + (3 - sections.size()) + " - " + this.sections.getFirst().getCriteria() + " - " + this.sections.getFirst().getPenalty() + "].");
		else
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "CardState -> Combat Zone Announce State! [Section " + (3 - sections.size()) + " - " + this.sections.getFirst().getCriteria() + " - " + this.sections.getFirst().getPenalty() + "].");
		for (Player p : this.state.getOrder(CardOrder.NORMAL)) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + p.voyageInfo(this.state.getPlanche()));
		}
	}

	/**
	 * Validates the {@link it.polimi.ingsw.message.server.ServerMessage} and if everyone has progressed, transitions.
	 *
	 * @param message {@link it.polimi.ingsw.message.server.ServerMessage} The message received from the player
	 * @throws ForbiddenCallException if the message is not allowed
	 */
	@Override
	public void validate(ServerMessage message) throws ForbiddenCallException {
		message.receive(this);
		if (!awaiting.isEmpty()) {
			this.state.broadcastMessage(new NotifyStateUpdateMessage(this.state.getClientState()));
			return;
		}
		this.target = this.state.findCriteria(this.sections.getFirst().getCriteria());
		if (target != null)
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Applying penalty to player: '" + target.getUsername() + "'");
		this.transition();
	}

	@Override
	public ClientCardState getClientCardState() {
		return new ClientCombatZoneIndexCardStateDecorator(
				new ClientAwaitConfirmCardStateDecorator(
						new ClientBaseCardState(
								this.getClass().getSimpleName(),
								card_id),
						new ArrayList<>(this.awaiting.stream().map(p -> p.getColor()).toList())),
				this.sections.getFirst(),
				3 - this.sections.size());
	}

	/**
	 * Computes and returns the next {@code CardState}.
	 *
	 * @return {@link CardState} the next state, or {@code null} if the card is exhausted
	 */
	@Override
	public CardState getNext() {
		if (this.state.getOrder(CardOrder.NORMAL).size() > 1)
			return new CombatZonePenaltyState(state, card_id, sections, shots, target);
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "...Card exhausted, moving to a new one!");
		return null;
	}

	/**
	 * Called when a {@link it.polimi.ingsw.model.player.Player} attempts to power a component.
	 *
	 * @param p {@link it.polimi.ingsw.model.player.Player} The player
	 * @param target_coords {@link it.polimi.ingsw.model.player.ShipCoords} the component to power
	 * @param battery_coords {@link it.polimi.ingsw.model.player.ShipCoords} the battery to use
	 */
	@Override
	public void turnOn(Player p, ShipCoords target_coords, ShipCoords battery_coords) {
		if (!this.awaiting.contains(p)) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to turn on a component after motioning to progress!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to turn on a component after motioning to progress!"));
			return;
		}
		try {
			p.getSpaceShip().turnOn(target_coords, battery_coords);
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' turned on component at" + target_coords + " using battery from " + battery_coords + "!");
		} catch (IllegalTargetException e) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to turn on a component with invalid coordinates!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to turn on a component with invalid coordinates!"));
		}
	}

	/**
	 * Called when a {@link it.polimi.ingsw.model.player.Player} attempts to progress their turn.
	 *
	 * @param p {@link it.polimi.ingsw.model.player.Player} The player
	 */
	@Override
	public void progressTurn(Player p) {
		if (!this.awaiting.contains(p)) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to progress the turn while already having done so!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to progress the turn while already having done so!"));
			return;
		}
		this.awaiting.remove(p);
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' motioned to progress! (" + (this.state.getCount().getNumber() - this.awaiting.size()) + ").");
	}

	/**
	 * Called when a {@link it.polimi.ingsw.model.player.Player} disconnects.
	 *
	 * @param p {@link it.polimi.ingsw.model.player.Player} The player disconnecting.
	 * @throws ForbiddenCallException when the state refuses the action.
	 */
	@Override
	public void disconnect(Player p) throws ForbiddenCallException {
		if (awaiting.contains(p)) {
			this.awaiting.remove(p);
		}

	}

}
