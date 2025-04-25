package it.polimi.ingsw.model.cards.state;

import java.util.ArrayList;

import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.cards.SmugglersCard;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.cards.visitors.ContainsRemoveVisitor;
import it.polimi.ingsw.model.client.card.ClientCardState;
import it.polimi.ingsw.model.client.card.ClientCargoPenaltyCardStateDecorator;
import it.polimi.ingsw.model.client.state.ClientModelState;
import it.polimi.ingsw.model.client.card.ClientBaseCardState;
import it.polimi.ingsw.model.components.enums.ShipmentType;
import it.polimi.ingsw.model.components.exceptions.ContainerEmptyException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.VoyageState;

class SmugglersLoseState extends CardState {

	private final SmugglersCard card;
	private final ArrayList<Player> list;
	private final int[] required;
	private boolean responded = false;

	public SmugglersLoseState(VoyageState state, SmugglersCard card, ArrayList<Player> list) {
		super(state);
		if (state == null || card == null || list == null || list.size() > this.state.getCount().getNumber() || list.size() < 1)
			throw new IllegalArgumentException("Created unsatisfyable state");
		this.card = card;
		this.list = list;
		this.required = new int[5];
		int penalty = this.card.getCargoPenalty();
		int[] player_cargo = list.getFirst().getSpaceShip().getContains();
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
	}

	@Override
	public void validate(ServerMessage message) throws ForbiddenCallException {
		message.receive(this);
		if (!responded) {
			this.state.broadcastMessage(new NotifyStateUpdateMessage(this.state.getClientState()));
			return;
		}
		this.transition();
	}

	@Override
	public ClientCardState getClientCardState() {
		return new ClientCargoPenaltyCardStateDecorator(
				new ClientBaseCardState(card.getId()),
				this.list.getFirst().getColor(),
				required
		);
	}

	@Override
	protected CardState getNext() {
		if (this.list.getFirst().getRetired() || this.list.getFirst().getDisconnected()) {
			this.list.removeFirst();
			if (!this.list.isEmpty()) return new SmugglersAnnounceState(state, card, list);
			return null;
		}
		this.list.removeFirst();
		if (!list.isEmpty()) return new SmugglersAnnounceState(state, card, list);
		return null;
	}

	@Override
	public void discardCargo(Player p, ShipmentType type, ShipCoords coords) {
		if (p != this.list.getFirst()) {
			System.out.println("Player '" + p.getUsername() + "' attempted to discard cargo during another player's turn!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to discard cargo during another player's turn!"));
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
		if (this.list.getFirst() == p) {
			this.responded = true;
		}
		this.list.remove(p);
	}

}
