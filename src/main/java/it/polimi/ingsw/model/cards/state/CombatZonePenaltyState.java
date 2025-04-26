package it.polimi.ingsw.model.cards.state;

import java.util.ArrayList;
import java.util.Arrays;

import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.cards.utils.CombatZonePenalty;
import it.polimi.ingsw.model.cards.utils.CombatZoneSection;
import it.polimi.ingsw.model.cards.utils.ProjectileArray;
import it.polimi.ingsw.model.cards.visitors.ContainsRemoveVisitor;
import it.polimi.ingsw.model.cards.visitors.CrewRemoveVisitor;
import it.polimi.ingsw.model.client.card.ClientAwaitConfirmCardStateDecorator;
import it.polimi.ingsw.model.client.card.ClientBaseCardState;
import it.polimi.ingsw.model.client.card.ClientCardState;
import it.polimi.ingsw.model.client.card.ClientCargoPenaltyCardStateDecorator;
import it.polimi.ingsw.model.client.card.ClientCombatZoneIndexCardStateDecorator;
import it.polimi.ingsw.model.client.card.ClientCrewPenaltyCardStateDecorator;
import it.polimi.ingsw.model.client.card.ClientProjectileCardStateDecorator;
import it.polimi.ingsw.model.client.state.ClientModelState;
import it.polimi.ingsw.model.components.enums.ShipmentType;
import it.polimi.ingsw.model.components.exceptions.ContainerEmptyException;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.VoyageState;

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
		this.required = new int[5];
		int penalty = this.sections.getFirst().getAmount();
		int[] player_cargo = this.target.getSpaceShip().getContains();
		for (ShipmentType t : ShipmentType.values()) {
			if (t.getValue() == 0) break;
			int tmp = penalty - player_cargo[t.getValue() - 1];
			if (tmp <= 0) {
				this.required[t.getValue() - 1] = penalty;
				return;
			} else {
				this.required[t.getValue() - 1] = player_cargo[t.getValue() - 1];
				penalty = tmp;
			}
		}
		if (penalty > 0) {
			this.required[4] = penalty;
		}
	}

	@Override
	public void init(ClientModelState new_state) {
		super.init(new_state);
		System.out.println("    CardState -> Combat Zone New Cabin State!: ["+(3-sections.size())+" - "+this.sections.getFirst().getPenalty()+"].");
		System.out.println("    Targeting: '"+this.target.getUsername()+"'.");
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
			this.target.getSpaceShip().handleShot(this.shots.getProjectiles().get(0));
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
								new ArrayList<>(Arrays.asList(this.target.getColor()))),
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
			System.out.println("Card exhausted, moving to a new one!");
			return null;
		}
		if (!target.getSpaceShip().getBrokeCenter()&&this.sections.getFirst().getPenalty()==CombatZonePenalty.SHOTS) target.getSpaceShip().verifyAndClean();
		this.shots.getProjectiles().removeFirst();
		if (this.target.getSpaceShip().getBrokeCenter())
			return new CombatZoneNewCabinState(state, card_id, sections, shots, target);
		if (this.sections.getFirst().getPenalty() == CombatZonePenalty.SHOTS && !this.shots.getProjectiles().isEmpty()) {
			return new CombatZonePenaltyState(state, card_id, sections, shots, target);
		}
		this.sections.removeFirst();
		if (!this.sections.isEmpty()) return new CombatZoneAnnounceState(state, card_id, sections, shots);
		System.out.println("Card exhausted, moving to a new one!");
		return null;
	}

	@Override
	public void turnOn(Player p, ShipCoords target_coords, ShipCoords battery_coords) {
		if (p != this.target) {
			System.out.println("Player '" + p.getUsername() + "' attempted to turn on a component during another player's turn!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to turn on a component during another player's turn!"));
			return;
		}
		if (this.sections.getFirst().getPenalty() != CombatZonePenalty.SHOTS) {
			System.out.println("Player '" + p.getUsername() + "' attempted to turn on a component when the penalty doesn't allow it!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to turn on a component when the penalty doesn't allow it!"));
			return;
		}
		try {
			p.getSpaceShip().turnOn(target_coords, battery_coords);
			System.out.println("Player '" + p.getUsername() + "' turned on component at"+target_coords+" using battery from "+battery_coords+"!");
		} catch (IllegalTargetException e) {
			System.out.println(e.getMessage());
			System.out.println("Player '" + p.getUsername() + "' attempted to turn on a component with invalid coordinates!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to turn on a component with invalid coordinates!"));
		}
	}

	@Override
	public void progressTurn(Player p) {
		if (p != this.target) {
			System.out.println("Player '" + p.getUsername() + "' attempted to progress during another player's turn!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to progress during another player's turn!"));
			return;
		}
		if (this.sections.getFirst().getPenalty() != CombatZonePenalty.SHOTS) {
			System.out.println("Player '" + p.getUsername() + "' attempted to progress when the penalty doesn't allow it!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to progress when the penalty doesn't allow it!"));
			return;
		}
		this.responded = true;
	}

	@Override
	public void removeCrew(Player p, ShipCoords cabin_coords) throws ForbiddenCallException {
		if (p != this.target) {
			System.out.println("Player '" + p.getUsername() + "' attempted to remove a crew member during another player's turn!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to remove a crew member during another player's turn!"));
			return;
		}
		if (this.sections.getFirst().getPenalty() != CombatZonePenalty.CREW) {
			System.out.println("Player '" + p.getUsername() + "' attempted to remove a crew member when the penalty doesn't allow it!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to remove a crew member when the penalty doesn't allow it!"));
			return;
		}
		CrewRemoveVisitor v = new CrewRemoveVisitor(p.getSpaceShip());
		try {
			p.getSpaceShip().getComponent(cabin_coords).check(v);
			this.amount++;
			System.out.println("Player '" + p.getUsername() + "' removed a crewmate from "+cabin_coords+"!");
		} catch (IllegalTargetException e) {
			System.out.println("Player '" + p.getUsername() + "' attempted to remove a crew member from invalid coordinates!");
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
	public void discardCargo(Player p, ShipmentType type, ShipCoords coords) {
		if (p != this.target) {
			System.out.println("Player '" + p.getUsername() + "' attempted to discard cargo during another player's turn!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to discard cargo during another player's turn!"));
			return;
		}
		if (this.sections.getFirst().getPenalty() != CombatZonePenalty.CARGO) {
			System.out.println("Player '" + p.getUsername() + "' attempted to discard cargo when the penalty doesn't allow it!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to discard cargo when the penalty doesn't allow it!"));
			return;
		}
		for (ShipmentType t : ShipmentType.values()) {
			if (t.getValue() == 0) break;
			if (this.required[t.getValue() - 1] <= 0) continue;
			if (t != type) {
				System.out.println("Player '" + p.getUsername() + "' attempted to discard cargo that's not his most valuable!");
				this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to discard cargo that's not his most valuable!"));
				return;
			}
			ContainsRemoveVisitor v = new ContainsRemoveVisitor(t);
			try {
				p.getSpaceShip().getComponent(coords).check(v);
				System.out.println("Player '" + p.getUsername() + "' discarded cargo type: "+type+" from "+coords+"!");
				this.required[t.getValue() - 1]--;
				break;
			} catch (ContainerEmptyException e) {
				System.out.println("Player '" + p.getUsername() + "' attempted to discard cargo from a storage that doesn't contain it!");
				this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to discard cargo from a storage that doesn't contain it!"));
				return;
			} catch (IllegalArgumentException e) {
				System.out.println("Player '" + p.getUsername() + "' attempted to discard cargo from illegal coordinates!");
				this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to discard cargo from illegal coordinates!"));
				return;
			}
		}
		if (this.required[4] > 0 && p.getSpaceShip().getEnergyPower() > 0) {
			ContainsRemoveVisitor v = new ContainsRemoveVisitor();
			try {
				p.getSpaceShip().getComponent(coords).check(v);
				System.out.println("Player '"+p.getUsername()+"' removed battery from "+coords);
			} catch (ContainerEmptyException e) {
				System.out.println("Player '" + p.getUsername() + "' attempted to discard batteries from a container that doesn't contain any!");
				this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to discard batteries from a container that doesn't contain any!"));
			} catch (IllegalArgumentException e) {
				System.out.println("Player '" + p.getUsername() + "' attempted to discard batteries from illegal coordinates!");
				this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to discard batteries from illegal coordinates!"));
			}
		} else this.responded = true;
	}

	@Override
	public void disconnect(Player p) throws ForbiddenCallException {
		if (this.target.equals(p)) {
			this.responded = true;
		}
		System.out.println("Player '" + p.getUsername() + "' disconnected!");
	}

}
