package it.polimi.ingsw.model.cards.state;

import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.cards.SlaversCard;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.client.card.ClientAwaitConfirmCardStateDecorator;
import it.polimi.ingsw.model.client.card.ClientBaseCardState;
import it.polimi.ingsw.model.client.card.ClientCardState;
import it.polimi.ingsw.model.client.state.ClientState;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.VoyageState;

import java.util.ArrayList;
import java.util.Collections;

public class SlaversAnnounceState extends CardState {

	private final SlaversCard card;
	private final ArrayList<Player> list;
	private boolean responded = false;
	private boolean result = false;

	public SlaversAnnounceState(VoyageState state, SlaversCard card, ArrayList<Player> list) {
		super(state);
		if (list.size() > this.state.getCount().getNumber() || list.size() < 1 || list == null)
			throw new IllegalArgumentException("Constructed insatisfyable state");
		if (card == null) throw new NullPointerException();
		this.card = card;
		this.list = list;
	}

	@Override
	public void init(ClientState new_state) {
		super.init(new_state);
		if (list.size() == this.state.getCount().getNumber())
			/*XXX*/System.out.println("New CardState -> Slavers Announce State!");
		else /*XXX*/System.out.println("    CardState -> Slavers Announce State!");
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
		if (!this.list.getFirst().getDisconnected()) result = this.card.apply(this.state, this.list.getFirst());
		this.transition();
	}

	@Override
	public ClientCardState getClientCardState() {
		ArrayList<PlayerColor> awaiting = new ArrayList<>(Collections.singletonList(this.list.getFirst().getColor()));
		return new ClientAwaitConfirmCardStateDecorator(new ClientBaseCardState(this.card.getId()), awaiting);
	}

	@Override
	public CardState getNext() {
		if (this.list.getFirst().getDisconnected() || this.list.getFirst().getRetired()) {
			this.list.removeFirst();
			if (!this.list.isEmpty()) return new SlaversAnnounceState(state, card, list);
			/*XXX*/System.out.println("Card exhausted, moving to a new one!");
			return null;
		}
		if (!result) return new SlaversLoseState(state, card, list);
		if (this.card.getExhausted()) return new SlaversRewardState(state, card, list);
		this.list.removeFirst();
		if (!this.list.isEmpty()) return new SlaversAnnounceState(state, card, list);
		/*XXX*/System.out.println("Card exhausted, moving to a new one!");
		return null;
	}

	@Override
	public void turnOn(Player p, ShipCoords target_coords, ShipCoords battery_coords) {
		if (p != this.list.getFirst()) {
			/*XXX*/System.out.println("Player '" + p.getUsername() + "' attempted to turn on a component during another player's turn!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to turn on a component during another player's turn!"));
			return;
		}
		try {
			p.getSpaceShip().turnOn(target_coords, battery_coords);
			/*XXX*/System.out.println("Player '" + p.getUsername() + "' turned on component at" + target_coords + " using battery from " + battery_coords + "!");
		} catch (IllegalTargetException e) {
			/*XXX*/System.out.println("Player '" + p.getUsername() + "' attempted to turn on a component with invalid coordinates!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to turn on a component with invalid coordinates!"));
		}
	}

	@Override
	public void progressTurn(Player p) {
		if (p != this.list.getFirst()) {
			/*XXX*/System.out.println("Player '" + p.getUsername() + "' attempted to progress during another player's turn!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to progress during another player's turn!"));
			return;
		}
		/*XXX*/System.out.println("Player '" + p.getUsername() + "' motioned to progress!");
		this.responded = true;
	}

	@Override
	public void disconnect(Player p) throws ForbiddenCallException {
		if (this.list.getFirst() == p) {

			this.responded = true;
		}
		this.list.remove(p);

	}


}
