package it.polimi.ingsw.model.cards.state;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.cards.OpenSpaceCard;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.cards.utils.CardOrder;
import it.polimi.ingsw.model.client.card.ClientAwaitConfirmCardStateDecorator;
import it.polimi.ingsw.model.client.card.ClientBaseCardState;
import it.polimi.ingsw.model.client.card.ClientCardState;
import it.polimi.ingsw.model.client.state.ClientModelState;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.VoyageState;

public class OpenSpaceState extends CardState {

	private final OpenSpaceCard card;
	private final ArrayList<Player> awaiting;

	public OpenSpaceState(VoyageState state, OpenSpaceCard card) {
		super(state);
		if (card == null) throw new NullPointerException();
		this.card = card;
		this.awaiting = new ArrayList<>(this.state.getOrder(CardOrder.NORMAL));
	}

	@Override
	public void init(ClientModelState new_state) {
		super.init(new_state);
		System.out.println("New CardState -> Open Space State!");
		for(Player p : this.state.getOrder(CardOrder.NORMAL)){
			System.out.println("	 - "+p.getUsername());
		}
	}

	@Override
	public void validate(ServerMessage message) throws ForbiddenCallException {
		message.receive(this);
		if (!awaiting.isEmpty()) {
			this.state.broadcastMessage(new NotifyStateUpdateMessage(this.state.getClientState()));
			return;
		}
		for (Player p : this.state.getOrder(CardOrder.NORMAL)) {
			this.card.apply(state, p);
		}
		this.transition();
	}

	@Override
	public ClientCardState getClientCardState() {
		List<PlayerColor> tmp = this.awaiting.stream().map(p -> p.getColor()).toList();
		return new ClientAwaitConfirmCardStateDecorator(new ClientBaseCardState(card.getId()), new ArrayList<>(tmp));
	}

	@Override
    public CardState getNext() {
		System.out.println("Card exhausted, moving to a new one!");
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
			System.out.println("Player '" + p.getUsername() + "' turned on component at"+target_coords+" using battery from "+battery_coords+"!");
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
		System.out.println("Player '" + p.getUsername() + "' motioned to progress! ("+(this.state.getCount().getNumber()-this.awaiting.size())+").");
		this.awaiting.remove(p);
	}

	@Override
	public void disconnect(Player p) throws ForbiddenCallException {
		this.awaiting.remove(p);
		System.out.println("Player '" + p.getUsername() + "' disconnected!");
	}

}
