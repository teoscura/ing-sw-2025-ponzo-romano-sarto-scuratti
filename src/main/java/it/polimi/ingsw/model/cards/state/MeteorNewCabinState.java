package it.polimi.ingsw.model.cards.state;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.cards.utils.CardOrder;
import it.polimi.ingsw.model.cards.utils.ProjectileArray;
import it.polimi.ingsw.model.client.card.ClientBaseCardState;
import it.polimi.ingsw.model.client.card.ClientCardState;
import it.polimi.ingsw.model.client.card.ClientNewCenterCardStateDecorator;
import it.polimi.ingsw.model.client.state.ClientModelState;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.VoyageState;

class MeteorNewCabinState extends CardState {

	private final int card_id;
	private final ProjectileArray left;

	public MeteorNewCabinState(VoyageState state, int card_id, ProjectileArray left) {
		super(state);
		if (left == null) throw new NullPointerException();
		if (card_id < 1 || card_id > 120) throw new IllegalArgumentException();
		this.card_id = card_id;
		this.left = left;
	}

	@Override
	public void init(ClientModelState new_state) {
		super.init(new_state);
		System.out.println("    CardState -> Meteor Swarm New Cabin State!");
	}

	@Override
	public void validate(ServerMessage message) throws ForbiddenCallException {
		message.receive(this);
		boolean missing = false;
		for (Player p : this.state.getOrder(CardOrder.NORMAL)) {
			missing = missing || p.getSpaceShip().getBrokeCenter();
		}
		if (missing) {
			this.state.broadcastMessage(new NotifyStateUpdateMessage(this.state.getClientState()));
			return;
		}
		this.transition();
	}

	@Override
	public ClientCardState getClientCardState() {
		List<PlayerColor> tmp = new ArrayList<>();
		for (Player p : this.state.getOrder(CardOrder.NORMAL)) {
			if (p.getSpaceShip().getBrokeCenter()) tmp.add(p.getColor());
		}
		return new ClientNewCenterCardStateDecorator(new ClientBaseCardState(card_id), new ArrayList<>(tmp));
	}

	@Override
    public CardState getNext() {
		this.left.getProjectiles().removeFirst();
		if (!this.left.getProjectiles().isEmpty()) return new MeteorAnnounceState(state, card_id, left);
		System.out.println("Card exhausted, moving to a new one!");
		return null;
	}

	@Override
	public void setNewShipCenter(Player p, ShipCoords new_center) {
		try {
			p.getSpaceShip().setCenter(new_center);
			System.out.println("Player '"+p.getUsername()+"' set a new ship center at "+new_center+".");
		} catch (IllegalTargetException e) {
			System.out.println("Player '" + p.getUsername() + "' attempted to set his new center on an empty space!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to set his new center on an empty space!"));
		} catch (ForbiddenCallException e) {
			System.out.println("Player '" + p.getUsername() + "' attempted to set his new center while having a unbroken ship!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to set his new center while having a unbroken ship!"));
		}
	}

	@Override
	public void disconnect(Player p) throws ForbiddenCallException {
		if (p.getSpaceShip().getBrokeCenter()) {
			this.state.loseGame(p);
		}
		System.out.println("Player '" + p.getUsername() + "' disconnected!");
	}

}
