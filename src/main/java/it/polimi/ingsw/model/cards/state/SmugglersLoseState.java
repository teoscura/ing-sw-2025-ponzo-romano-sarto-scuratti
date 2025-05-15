package it.polimi.ingsw.model.cards.state;

import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.cards.SmugglersCard;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.cards.visitors.ContainsRemoveVisitor;
import it.polimi.ingsw.model.client.card.ClientBaseCardState;
import it.polimi.ingsw.model.client.card.ClientCardState;
import it.polimi.ingsw.model.client.card.ClientCargoPenaltyCardStateDecorator;
import it.polimi.ingsw.model.client.state.ClientState;
import it.polimi.ingsw.model.components.enums.ShipmentType;
import it.polimi.ingsw.model.components.exceptions.ContainerEmptyException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.VoyageState;

import java.util.ArrayList;

public class SmugglersLoseState extends CardState {

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
		this.required = new int[]{0, 0, 0, 0, 0};
		int pen = this.card.getCargoPenalty();
		int idx = 4;
		while (pen >= 1) {
			if (idx < 0) break;
			this.required[idx] = pen - this.list.getFirst().getSpaceShip().getContains()[idx] >= 0 ? this.list.getFirst().getSpaceShip().getContains()[idx] : pen;
			pen -= this.required[idx];
			idx--;
		}
	}

	@Override
	public void init(ClientState new_state) {
		super.init(new_state);
		/*XXX*/System.out.println("    CardState -> Smugglers Lose State!");
		/*XXX*/System.out.println("    Bat: " + required[0] + " Blu: " + required[1] + " Grn: " + required[2] + " Ylw: " + required[3] + " Red: " + required[4]);
		for (Player p : this.list) {
			/*XXX*/System.out.println("	 - " + p.getUsername());
		}
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
	public CardState getNext() {
		if (this.list.getFirst().getDisconnected()) {
			this.list.removeFirst();
			if (!this.list.isEmpty()) return new SmugglersAnnounceState(state, card, list);
			/*XXX*/System.out.println("Card exhausted, moving to a new one!");
			return null;
		}
		this.list.removeFirst();
		if (!list.isEmpty()) return new SmugglersAnnounceState(state, card, list);
		/*XXX*/System.out.println("Card exhausted, moving to a new one!");
		return null;
	}

	@Override
	public void discardCargo(Player p, ShipmentType type, ShipCoords coords) {
		if (!p.equals(this.list.getFirst())) {
			/*XXX*/System.out.println("Player '" + p.getUsername() + "' attempted to discard cargo during another player's turn!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to discard cargo during another player's turn!"));
			return;
		}
		if (p.getSpaceShip().getContains()[type.getValue()] <= 0) {
			/*XXX*/System.out.println("Player '" + p.getUsername() + "' attempted to discard cargo that he doesn't own!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to discard cargo that he doesn't own!"));
			return;
		}
		int idx = 4;
		while (idx >= 0) {
			if (this.required[idx] <= 0) {
				idx--;
				continue;
			}
			if (type.getValue() != idx) {
				/*XXX*/System.out.println("Player '" + p.getUsername() + "' attempted to discard cargo that's not their most valuable!");
				this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to discard cargo that's not their most valuable!"));
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
			} catch (IllegalArgumentException e) {
				if (type != ShipmentType.EMPTY) {
					/*XXX*/System.out.println("Player '" + p.getUsername() + "' attempted to discard cargo from illegal coordinates!");
					this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to discard cargo from illegal coordinates!"));
				} else {
					/*XXX*/System.out.println("Player '" + p.getUsername() + "' attempted to discard a battery from illegal coordinates!");
					this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to discard a battery from illegal coordinates!"));
				}
			}
		}
		for (int i = 4; i >= 0; i--) {
			if (required[i] > 0) return;
		}
		this.responded = true;
	}

	@Override
	public void disconnect(Player p) throws ForbiddenCallException {
		if (this.list.getFirst().equals(p)) {
			this.responded = true;
			return;
		}
		this.list.remove(p);

	}

}
