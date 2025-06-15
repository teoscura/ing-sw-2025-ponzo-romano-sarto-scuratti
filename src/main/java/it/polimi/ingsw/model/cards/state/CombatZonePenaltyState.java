package it.polimi.ingsw.model.cards.state;

import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.cards.CombatZoneCard;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.cards.utils.CombatZonePenalty;
import it.polimi.ingsw.model.cards.utils.CombatZoneSection;
import it.polimi.ingsw.model.cards.utils.ProjectileArray;
import it.polimi.ingsw.model.cards.visitors.ContainsRemoveVisitor;
import it.polimi.ingsw.model.cards.visitors.CrewRemoveVisitor;
import it.polimi.ingsw.model.client.card.*;
import it.polimi.ingsw.model.client.state.ClientState;
import it.polimi.ingsw.model.components.enums.ShipmentType;
import it.polimi.ingsw.model.components.exceptions.ContainerEmptyException;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.VoyageState;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.LoggerLevel;

import java.util.ArrayList;
import java.util.List;
/**
 * Class representing a Penalty State of the {@link CombatZoneCard}.
 */
class CombatZonePenaltyState extends CardState {

	private final int card_id;
	private final ArrayList<CombatZoneSection> sections;
	private final ProjectileArray shots;
	private final Player target;
	private final int[] required;
	private boolean responded = false;
	private int amount = 0;

	/**
	 * Constructs a new {@code CombatZonePenaltyState}.
	 *
	 * @param state {@link VoyageState} The current voyage state
	 * @param card_id   The card id
	 * @param sections  The sections defining penalties
	 * @param shots     The projectile array
	 * @param target   The {@link Player} receiving the penalty
	 */
	public CombatZonePenaltyState(VoyageState state, int card_id, ArrayList<CombatZoneSection> sections, ProjectileArray shots, Player target) {
		super(state);
		if (sections == null || shots == null || target == null) ;
		if (card_id < 1 || card_id > 120) throw new IllegalArgumentException();
		this.card_id = card_id;
		this.sections = sections;
		this.shots = shots;
		this.target = target;
		if (this.sections.getFirst().getPenalty() != CombatZonePenalty.CARGO) {
			this.required = null;
			return;
		}
		this.required = new int[]{0, 0, 0, 0, 0};
		int pen = this.sections.getFirst().getAmount();
		int idx = 4;
		while (pen >= 1) {
			if (idx < 0) break;
			this.required[idx] = pen - this.target.getSpaceShip().getContains()[idx] >= 0 ? this.target.getSpaceShip().getContains()[idx] : pen;
			pen -= this.required[idx];
			idx--;
		}
	}

	/**
	 * Called when the card state is initialized.
	 * Resets power for all players ships.
	 * Applies the penalty if possible and immediately transitions.
	 *
	 * @param new_state {@link ClientState} The new client state to broadcast to all connected listeners.
	 */
	@Override
	public void init(ClientState new_state) {
		super.init(new_state);
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "CardState -> Combat Zone Penalty State!: [" + (3 - sections.size()) + " - " + this.sections.getFirst().getPenalty() + "].");
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Targeting: '" + this.target.getUsername() + "'.");
		switch (this.sections.getFirst().getPenalty()) {
			case CARGO: {
				Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Bat: " + required[0] + " Blu: " + required[1] + " Grn: " + required[2] + " Ylw: " + required[3] + " Red: " + required[4]);
				int total = 0;
				for (int t : this.required) total += t;
				if (total == 0) this.transition();
			}
			break;
			case CREW: {
				if (target.getSpaceShip().getCrew()[0] <= 0) this.transition();
			}
			break;
			case DAYS: {
				this.state.getPlanche().movePlayer(state, target, -sections.getFirst().getAmount());
				this.transition();
			}
			break;
			case SHOTS:
				return;
			default:
		}
	}

	/**
	 * Validates the {@link ServerMessage} and if the target has responded, transitions.
	 *
	 * @param message {@link ServerMessage} The message received from the player
	 * @throws ForbiddenCallException if the message is not allowed
	 */
	@Override
	public void validate(ServerMessage message) throws ForbiddenCallException {
		message.receive(this);
		if (!responded) {
			this.state.broadcastMessage(new NotifyStateUpdateMessage(this.state.getClientState()));
			return;
		}
		if (this.sections.getFirst().getPenalty() == CombatZonePenalty.SHOTS) {
			this.target.getSpaceShip().handleShot(this.shots.getProjectiles().getFirst());
		}
		this.transition();
	}

	@Override
	public ClientCardState getClientCardState() {
		switch (this.sections.getFirst().getPenalty()) {
			case CombatZonePenalty.CARGO:
				return new ClientCargoPenaltyCardStateDecorator(
						new ClientCombatZoneIndexCardStateDecorator(
								new ClientBaseCardState(
										this.getClass().getSimpleName(),
										card_id),
								this.sections.getFirst(),
								3 - this.sections.size()),
						target.getColor(),
						this.required);
			case CombatZonePenalty.CREW:
				return new ClientCrewPenaltyCardStateDecorator(
						new ClientCombatZoneIndexCardStateDecorator(
								new ClientBaseCardState(
										this.getClass().getSimpleName(),
										card_id),
								this.sections.getFirst(),
								3 - this.sections.size()),
						target.getColor(),
						this.sections.getFirst().getAmount() - this.amount);
			case CombatZonePenalty.SHOTS:
				return new ClientProjectileCardStateDecorator(
						new ClientAwaitConfirmCardStateDecorator(
								new ClientCombatZoneIndexCardStateDecorator(
										new ClientBaseCardState(
												this.getClass().getSimpleName(),
												card_id),
										this.sections.getFirst(),
										3 - this.sections.size()),
								new ArrayList<>(List.of(this.target.getColor()))),
						this.shots.getProjectiles().getFirst());
			default:
				return new ClientCombatZoneIndexCardStateDecorator(
						new ClientBaseCardState(
								this.getClass().getSimpleName(),
								card_id),
						this.sections.getFirst(),
						3 - this.sections.size());
		}
	}

	/**
	 * Computes and returns the next {@code CardState}.
	 *
	 * @return {@link CardState} the next state, or {@code null} if the card is exhausted
	 */
	@Override
	public CardState getNext() {
		if (this.target.getDisconnected()) {
			this.sections.removeFirst();
			if (!this.sections.isEmpty()) return new CombatZoneAnnounceState(state, card_id, sections, shots);
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "...Card exhausted, moving to a new one!");
			return null;
		}
		if (this.sections.getFirst().getPenalty() == CombatZonePenalty.SHOTS) this.shots.getProjectiles().removeFirst();
		if (this.target.getSpaceShip().getBlobsSize() > 1)
			return new CombatZoneSelectShipState(state, card_id, sections, shots, target);
		if (this.sections.getFirst().getPenalty() == CombatZonePenalty.SHOTS && !this.shots.getProjectiles().isEmpty()) {
			return new CombatZonePenaltyState(state, card_id, sections, shots, target);
		}
		this.sections.removeFirst();
		if (!this.sections.isEmpty()) return new CombatZoneAnnounceState(state, card_id, sections, shots);
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "...Card exhausted, moving to a new one!");
		return null;
	}

	/**
	 * Called when a {@link Player} attempts to power a component.
	 *
	 * @param p {@link Player} The player
	 * @param target_coords {@link ShipCoords} the component to power
	 * @param battery_coords {@link ShipCoords} the battery to use
	 * @throws ForbiddenCallException when the state refuses the action.
	 */
	@Override
	public void turnOn(Player p, ShipCoords target_coords, ShipCoords battery_coords) throws ForbiddenCallException {
		if (!p.equals(this.target)) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to turn on a component during another player's turn!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to turn on a component during another player's turn!"));
			return;
		}
		if (this.sections.getFirst().getPenalty() != CombatZonePenalty.SHOTS) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to turn on a component when the penalty doesn't allow it!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to turn on a component when the penalty doesn't allow it!"));
			throw new ForbiddenCallException();
		}
		try {
			p.getSpaceShip().turnOn(target_coords, battery_coords);
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' turned on component at" + target_coords + " using battery from " + battery_coords + "!");
		} catch (IllegalTargetException e) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + e.getMessage());
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to turn on a component with invalid coordinates!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to turn on a component with invalid coordinates!"));
		}
	}

	/**
	 * Called when a {@link Player} attempts to progress their turn.
	 *
	 * @param p {@link Player} The player
	 * @throws ForbiddenCallException when the state refuses the action.
	 */
	@Override
	public void progressTurn(Player p) throws ForbiddenCallException {
		if (!p.equals(this.target)) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to progress during another player's turn!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to progress during another player's turn!"));
			return;
		}
		if (this.sections.getFirst().getPenalty() != CombatZonePenalty.SHOTS) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to progress when the penalty doesn't allow it!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to progress when the penalty doesn't allow it!"));
			throw new ForbiddenCallException();
		}
		this.responded = true;
	}

	/**
	 * Called when a {@link Player} tries to remove crew from a cabin.
	 *
	 * @param p {@link Player} The player
	 * @param cabin_coords {@link ShipCoords} the coordinates of the cabin
	 * @throws ForbiddenCallException when the state refuses the action.
	 */
	@Override
	public void removeCrew(Player p, ShipCoords cabin_coords) throws ForbiddenCallException {
		if (!p.equals(this.target)) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to remove a crew member during another player's turn!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to remove a crew member during another player's turn!"));
			return;
		}
		if (this.sections.getFirst().getPenalty() != CombatZonePenalty.CREW) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to remove a crew member when the penalty doesn't allow it!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to remove a crew member when the penalty doesn't allow it!"));
			throw new ForbiddenCallException();
		}
		CrewRemoveVisitor v = new CrewRemoveVisitor(p.getSpaceShip());
		try {
			p.getSpaceShip().getComponent(cabin_coords).check(v);
			this.amount++;
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' removed a crewmate from " + cabin_coords + "!");
		} catch (IllegalTargetException e) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to remove a crew member from invalid coordinates!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to remove a crew member from invalid coordinates!"));
			return;
		}
		p.getSpaceShip().updateShip();
		if (this.amount == this.sections.getFirst().getAmount() || p.getSpaceShip().getCrew()[0] == 0) {
			this.responded = true;
		}
	}

	/**
	 * Called when a {@link Player} tries to discard cargo.
	 *
	 * @param p {@link Player} The player
	 * @param type {@link ShipmentType} The cargo type
	 * @param coords {@link ShipCoords} the coordinates of the target
	 * @throws ForbiddenCallException when the state refuses the action.
	 */
	@Override
	public void discardCargo(Player p, ShipmentType type, ShipCoords coords) throws ForbiddenCallException {
		if (!p.equals(this.target)) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to discard cargo during another player's turn!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to discard cargo during another player's turn!"));
			return;
		}
		if (this.sections.getFirst().getPenalty() != CombatZonePenalty.CARGO) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to discard cargo when the penalty doesn't allow it!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to discard cargo when the penalty doesn't allow it!"));
			throw new ForbiddenCallException();
		}
		int idx = 4;
		while (idx >= 0) {
			if (this.required[idx] <= 0) {
				idx--;
				continue;
			}
			if (type.getValue() != idx) {
				Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to discard cargo that's not his most valuable!");
				this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to discard cargo that's not his most valuable!"));
				return;
			}
			ContainsRemoveVisitor v = new ContainsRemoveVisitor(p.getSpaceShip(), type);
			try {
				p.getSpaceShip().getComponent(coords).check(v);
				this.required[idx]--;
				if (type != ShipmentType.EMPTY)
					Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' removed cargo type: " + type + " from " + coords);
				else
					Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' removed battery from " + coords);
				break;
			} catch (ContainerEmptyException e) {
				if (type != ShipmentType.EMPTY) {
					Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to discard cargo from a storage that doesn't contain it!");
					this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to discard cargo from a storage that doesn't contain it!"));
				} else {
					Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to discard a battery from a storage that doesn't contain it!");
					this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to discard a battery from a storage that doesn't contain it!"));
				}
				return;
			} catch (IllegalTargetException e) {
				if (type != ShipmentType.EMPTY) {
					Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to discard cargo from illegal coordinates!");
					this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to discard cargo from illegal coordinates!"));
				} else {
					Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to discard a battery from illegal coordinates!");
					this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to discard a battery from illegal coordinates!"));
				}
				return;
			}
		}
		for (int i = 4; i >= 0; i--) {
			if (required[i] > 0) return;
		}
		this.responded = true;
	}

	/**
	 * Called when a {@link Player} disconnects.
	 *
	 * @param p {@link Player} The player disconnecting.
	 * @throws ForbiddenCallException when the state refuses the action.
	 */
	@Override
	public void disconnect(Player p) throws ForbiddenCallException {
		if (this.target.equals(p)) {
			this.responded = true;
		}

	}

}
