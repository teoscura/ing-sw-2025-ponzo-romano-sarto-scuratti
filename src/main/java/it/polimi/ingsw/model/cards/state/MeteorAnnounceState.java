package it.polimi.ingsw.model.cards.state;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.cards.utils.CardOrder;
import it.polimi.ingsw.model.cards.utils.ProjectileArray;
import it.polimi.ingsw.model.client.card.ClientAwaitConfirmCardStateDecorator;
import it.polimi.ingsw.model.client.card.ClientBaseCardState;
import it.polimi.ingsw.model.client.card.ClientCardState;
import it.polimi.ingsw.model.client.card.ClientMeteoriteCardStateDecorator;
import it.polimi.ingsw.model.client.state.ClientModelState;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.VoyageState;

public class MeteorAnnounceState extends CardState {

	private final int card_id;
	private final ProjectileArray left;
	private final ArrayList<Player> awaiting;
	private boolean broke_cabin;

	public MeteorAnnounceState(VoyageState state, int card_id, ProjectileArray array) {
		super(state);
		if (array == null) throw new NullPointerException();
		if (card_id < 1 || card_id > 120) throw new IllegalArgumentException();
		this.card_id = card_id;
		this.left = array;
		this.awaiting = new ArrayList<>(this.state.getOrder(CardOrder.NORMAL));
	}

	@Override
	public void init(ClientModelState new_state) {
		super.init(new_state);
	}

	@Override
	public void validate(ServerMessage message) throws ForbiddenCallException {
		message.receive(this);
		if (!awaiting.isEmpty()) {
			this.state.broadcastMessage(new NotifyStateUpdateMessage(this.state.getClientState()));
			return;
		}
		for (Player p : this.state.getOrder(CardOrder.NORMAL)) {
			this.broke_cabin = p.getSpaceShip().handleMeteorite(this.left.getProjectiles().getFirst());
		}
		this.transition();
	}

	@Override
	public ClientCardState getClientCardState() {
		List<PlayerColor> tmp = this.awaiting.stream().map(p -> p.getColor()).toList();
		return new ClientMeteoriteCardStateDecorator(
				new ClientAwaitConfirmCardStateDecorator(
						new ClientBaseCardState(this.card_id),
						new ArrayList<>(tmp)),
				this.left.getProjectiles().getFirst());
	}

	@Override
    public CardState getNext() {
		for (Player p : this.state.getOrder(CardOrder.NORMAL)) {
			if (!p.getSpaceShip().getBrokeCenter()) p.getSpaceShip().verifyAndClean();
		}
		if (broke_cabin) return new MeteorNewCabinState(state, card_id, left);
		this.left.getProjectiles().removeFirst();
		if (!this.left.getProjectiles().isEmpty()) return new MeteorAnnounceState(state, card_id, left);
		return null;
	}

	@Override
	public void turnOn(Player p, ShipCoords target_coords, ShipCoords battery_coords) {
		if (!this.awaiting.contains(p)) {
			System.out.println("Player '" + p.getUsername() + "' attempted to turn on a component after motioning to progress!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to turn on a component after motioning to progress!"));
			return;
		}
		try {
			p.getSpaceShip().turnOn(target_coords, battery_coords);
		} catch (IllegalTargetException e) {
			System.out.println("Player '" + p.getUsername() + "' attempted to turn on a component with invalid coordinates!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to turn on a component with invalid coordinates!"));
		}
	}

	@Override
	public void progressTurn(Player p) {
		if (!this.awaiting.contains(p)) {
			System.out.println("Player '" + p.getUsername() + "' attempted to progress the turn while already having done so!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to progress the turn while already having done so!"));
			return;
		}
		this.awaiting.remove(p);
	}

	@Override
	public void disconnect(Player p) throws ForbiddenCallException {
		if (this.awaiting.contains(p)) {
			this.awaiting.remove(p);
		}
	}

}
