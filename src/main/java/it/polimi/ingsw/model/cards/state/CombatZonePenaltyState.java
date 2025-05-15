package it.polimi.ingsw.model.cards.state;

import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
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

import java.util.ArrayList;
import java.util.Collections;

class CombatZonePenaltyState extends CardState {

	private final int card_id;
	private final ArrayList<CombatZoneSection> sections;
	private final ProjectileArray shots;
	private final Player target;
	private final int[] required;
	private boolean responded = false;
	private int amount = 0;

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

	@Override
	public void init(ClientState new_state) {
		super.init(new_state);
		/*XXX*/System.out.println("CardState -> Combat Zone Penalty State!: [" + (3 - sections.size()) + " - " + this.sections.getFirst().getPenalty() + "].");
		/*XXX*/System.out.println("Targeting: '" + this.target.getUsername() + "'.");
		if (sections.getFirst().getPenalty() == CombatZonePenalty.CARGO)
			/*XXX*/System.out.println("Bat: " + required[0] + " Blu: " + required[1] + " Grn: " + required[2] + " Ylw: " + required[3] + " Red: " + required[4]);
		if (sections.getFirst().getPenalty() != CombatZonePenalty.DAYS) return;
		this.state.getPlanche().movePlayer(state, target, -sections.getFirst().getAmount());
		this.transition();
	}

	@Override
	public void validate(ServerMessage message) throws ForbiddenCallException {
		message.receive(this);
		if (!responded && !this.target.getRetired()) {
			this.state.broadcastMessage(new NotifyStateUpdateMessage(this.state.getClientState()));
			return;
		}
		if (this.sections.getFirst().getPenalty() == CombatZonePenalty.SHOTS) {
			this.target.getSpaceShip().handleShot(this.shots.getProjectiles().getFirst());
			if (this.target.getSpaceShip().getBlobsSize() <= 0) this.state.loseGame(target);
		}
		this.transition();
	}

	@Override
	public ClientCardState getClientCardState() {
		switch (this.sections.getFirst().getPenalty()) {
			case CombatZonePenalty.CARGO:
				return new ClientCargoPenaltyCardStateDecorator(
						new ClientCombatZoneIndexCardStateDecorator(
								new ClientBaseCardState(card_id),
								3 - this.sections.size()),
						target.getColor(),
						this.required);
			case CombatZonePenalty.CREW:
				return new ClientCrewPenaltyCardStateDecorator(
						new ClientCombatZoneIndexCardStateDecorator(
								new ClientBaseCardState(card_id),
								3 - this.sections.size()),
						target.getColor(),
						this.sections.getFirst().getAmount());
			case CombatZonePenalty.SHOTS:
				return new ClientProjectileCardStateDecorator(
						new ClientAwaitConfirmCardStateDecorator(
								new ClientCombatZoneIndexCardStateDecorator(
										new ClientBaseCardState(card_id),
										3 - this.sections.size()),
								new ArrayList<>(Collections.singletonList(this.target.getColor()))),
						this.shots.getProjectiles().getFirst());
			default:
				return new ClientCombatZoneIndexCardStateDecorator(
						new ClientBaseCardState(card_id),
						3 - this.sections.size());
		}
	}

	@Override
	public CardState getNext() {
		if (this.target.getRetired() || this.target.getDisconnected()) {
			this.sections.removeFirst();
			if (!this.sections.isEmpty()) return new CombatZoneAnnounceState(state, card_id, sections, shots);
			/*XXX*/System.out.println("...Card exhausted, moving to a new one!");
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
		/*XXX*/System.out.println("...Card exhausted, moving to a new one!");
		return null;
	}

	@Override
	public void turnOn(Player p, ShipCoords target_coords, ShipCoords battery_coords) throws ForbiddenCallException {
		if (!p.equals(this.target)) {
			/*XXX*/System.out.println("Player '" + p.getUsername() + "' attempted to turn on a component during another player's turn!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to turn on a component during another player's turn!"));
			return;
		}
		if (this.sections.getFirst().getPenalty() != CombatZonePenalty.SHOTS) {
			/*XXX*/System.out.println("Player '" + p.getUsername() + "' attempted to turn on a component when the penalty doesn't allow it!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to turn on a component when the penalty doesn't allow it!"));
			throw new ForbiddenCallException();
		}
		try {
			p.getSpaceShip().turnOn(target_coords, battery_coords);
			/*XXX*/System.out.println("Player '" + p.getUsername() + "' turned on component at" + target_coords + " using battery from " + battery_coords + "!");
		} catch (IllegalTargetException e) {
			/*XXX*/System.out.println(e.getMessage());
			/*XXX*/System.out.println("Player '" + p.getUsername() + "' attempted to turn on a component with invalid coordinates!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to turn on a component with invalid coordinates!"));
		}
	}

	@Override
	public void progressTurn(Player p) throws ForbiddenCallException {
		if (!p.equals(this.target)) {
			/*XXX*/System.out.println("Player '" + p.getUsername() + "' attempted to progress during another player's turn!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to progress during another player's turn!"));
			return;
		}
		if (this.sections.getFirst().getPenalty() != CombatZonePenalty.SHOTS) {
			/*XXX*/System.out.println("Player '" + p.getUsername() + "' attempted to progress when the penalty doesn't allow it!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to progress when the penalty doesn't allow it!"));
			throw new ForbiddenCallException();
		}
		this.responded = true;
	}

	@Override
	public void removeCrew(Player p, ShipCoords cabin_coords) throws ForbiddenCallException {
		if (!p.equals(this.target)) {
			/*XXX*/System.out.println("Player '" + p.getUsername() + "' attempted to remove a crew member during another player's turn!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to remove a crew member during another player's turn!"));
			return;
		}
		if (this.sections.getFirst().getPenalty() != CombatZonePenalty.CREW) {
			/*XXX*/System.out.println("Player '" + p.getUsername() + "' attempted to remove a crew member when the penalty doesn't allow it!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to remove a crew member when the penalty doesn't allow it!"));
			throw new ForbiddenCallException();
		}
		CrewRemoveVisitor v = new CrewRemoveVisitor(p.getSpaceShip());
		try {
			p.getSpaceShip().getComponent(cabin_coords).check(v);
			this.amount++;
			/*XXX*/System.out.println("Player '" + p.getUsername() + "' removed a crewmate from " + cabin_coords + "!");
		} catch (IllegalTargetException e) {
			/*XXX*/System.out.println("Player '" + p.getUsername() + "' attempted to remove a crew member from invalid coordinates!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to remove a crew member from invalid coordinates!"));
			return;
		}
		p.getSpaceShip().updateShip();
		if (p.getSpaceShip().getCrew()[0] == 0) {
			this.state.loseGame(p);
			return;
		}
		if (this.amount == this.sections.getFirst().getAmount()) {
			this.responded = true;
		}
	}

	@Override
	public void discardCargo(Player p, ShipmentType type, ShipCoords coords) throws ForbiddenCallException {
		if (!p.equals(this.target)) {
			/*XXX*/System.out.println("Player '" + p.getUsername() + "' attempted to discard cargo during another player's turn!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to discard cargo during another player's turn!"));
			return;
		}
		if (this.sections.getFirst().getPenalty() != CombatZonePenalty.CARGO) {
			/*XXX*/System.out.println("Player '" + p.getUsername() + "' attempted to discard cargo when the penalty doesn't allow it!");
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
				/*XXX*/System.out.println("Player '" + p.getUsername() + "' attempted to discard cargo that's not his most valuable!");
				this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to discard cargo that's not his most valuable!"));
				return;
			}
			ContainsRemoveVisitor v = new ContainsRemoveVisitor(p.getSpaceShip(), type);
			try {
				p.getSpaceShip().getComponent(coords).check(v);
				this.required[idx]--;
				if (type != ShipmentType.EMPTY)
					/*XXX*/System.out.println("Player '" + p.getUsername() + "' removed cargo type: " + type + " from " + coords);
				else /*XXX*/System.out.println("Player '" + p.getUsername() + "' removed battery from " + coords);
				break;
			} catch (ContainerEmptyException e) {
				if (type != ShipmentType.EMPTY) {
					/*XXX*/System.out.println("Player '" + p.getUsername() + "' attempted to discard cargo from a storage that doesn't contain it!");
					this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to discard cargo from a storage that doesn't contain it!"));
				} else {
					/*XXX*/System.out.println("Player '" + p.getUsername() + "' attempted to discard a battery from a storage that doesn't contain it!");
					this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to discard a battery from a storage that doesn't contain it!"));
				}
				return;
			} catch (IllegalTargetException e) {
				if (type != ShipmentType.EMPTY) {
					/*XXX*/System.out.println("Player '" + p.getUsername() + "' attempted to discard cargo from illegal coordinates!");
					this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to discard cargo from illegal coordinates!"));
				} else {
					/*XXX*/System.out.println("Player '" + p.getUsername() + "' attempted to discard a battery from illegal coordinates!");
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

	@Override
	public void disconnect(Player p) throws ForbiddenCallException {
		if (this.target.equals(p)) {
			this.responded = true;
		}

	}

}
