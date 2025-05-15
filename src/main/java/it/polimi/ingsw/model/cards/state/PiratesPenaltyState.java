package it.polimi.ingsw.model.cards.state;

import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.cards.PiratesCard;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.cards.utils.ProjectileArray;
import it.polimi.ingsw.model.client.card.ClientAwaitConfirmCardStateDecorator;
import it.polimi.ingsw.model.client.card.ClientBaseCardState;
import it.polimi.ingsw.model.client.card.ClientCardState;
import it.polimi.ingsw.model.client.card.ClientProjectileCardStateDecorator;
import it.polimi.ingsw.model.client.state.ClientState;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.VoyageState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PiratesPenaltyState extends CardState {

	private final PiratesCard card;
	private final ArrayList<Player> list;
	private final ProjectileArray shots;
	private boolean responded = false;

	protected PiratesPenaltyState(VoyageState state, PiratesCard card, ArrayList<Player> list, ProjectileArray shots) {
		super(state);
		if (list.size() > this.state.getCount().getNumber() || list.size() < 1 || list == null)
			throw new IllegalArgumentException("Constructed insatisfyable state");
		if (card == null || shots == null) throw new NullPointerException();
		this.card = card;
		this.list = list;
		this.shots = shots;
	}

	@Override
	public void init(ClientState new_state) {
		super.init(new_state);
		System.out.println("    CardState -> Pirates Penalty State!");
		for (Player p : this.list) {
			System.out.println("	 - " + p.getUsername());
		}
	}

	@Override
	public void validate(ServerMessage message) throws ForbiddenCallException {
		message.receive(this);
		if (!responded) {
			this.state.broadcastMessage(new NotifyStateUpdateMessage(this.state.getClientState()));
			return;
		}
		if (!this.list.getFirst().getDisconnected()) {
			this.list.getFirst().getSpaceShip().handleShot(this.shots.getProjectiles().getFirst());
			if (this.list.getFirst().getSpaceShip().getBlobsSize() <= 0) this.state.loseGame(this.list.getFirst());
		}
		this.transition();
	}

	@Override
	public ClientCardState getClientCardState() {
		List<PlayerColor> awaiting = Collections.singletonList(this.list.getFirst().getColor());
		return new ClientProjectileCardStateDecorator(
				new ClientAwaitConfirmCardStateDecorator(
						new ClientBaseCardState(this.card.getId()),
						new ArrayList<>(awaiting)),
				this.shots.getProjectiles().getFirst());
	}

	@Override
	public CardState getNext() {
		if (this.list.getFirst().getRetired() || this.list.getFirst().getDisconnected()) {
			this.list.removeFirst();
			if (!this.list.isEmpty()) return new PiratesAnnounceState(state, card, list);
			System.out.println("Card exhausted, moving to a new one!");
			return null;
		}
		this.shots.getProjectiles().removeFirst();
		if (this.list.getFirst().getSpaceShip().getBlobsSize() > 1)
			return new PiratesSelectShipState(state, card, list, shots);
		if (!this.shots.getProjectiles().isEmpty()) return new PiratesPenaltyState(state, card, list, shots);
		this.list.removeFirst();
		if (!this.list.isEmpty()) return new PiratesAnnounceState(state, card, list);
		System.out.println("Card exhausted, moving to a new one!");
		return null;
	}

	@Override
	public void turnOn(Player p, ShipCoords target_coords, ShipCoords battery_coords) {
		if (!this.list.getFirst().equals(p)) {
			System.out.println("Player '" + p.getUsername() + "' attempted to turn on a component during another player's turn!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to turn on a component during another player's turn!"));
			return;
		}
		try {
			p.getSpaceShip().turnOn(target_coords, battery_coords);
			System.out.println("Player '" + p.getUsername() + "' turned on component at" + target_coords + " using battery from " + battery_coords + "!");
		} catch (IllegalTargetException e) {
			System.out.println("Player '" + p.getUsername() + "' attempted to turn on a component with invalid coordinates!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to turn on a component with invalid coordinates!"));
		}
	}

	@Override
	public void progressTurn(Player p) {
		if (!this.list.getFirst().equals(p)) {
			System.out.println("Player '" + p.getUsername() + "' attempted to progress during another player's turn!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to progress during another player's turn!"));
			return;
		}
		this.responded = true;
		System.out.println("Player '" + p.getUsername() + "' motioned to progress!");
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
