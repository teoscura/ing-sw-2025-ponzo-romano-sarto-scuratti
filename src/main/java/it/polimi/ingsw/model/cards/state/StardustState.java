package it.polimi.ingsw.model.cards.state;

import java.util.ArrayList;

import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.cards.StardustCard;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.cards.utils.CardOrder;
import it.polimi.ingsw.model.client.card.ClientAwaitConfirmCardStateDecorator;
import it.polimi.ingsw.model.client.card.ClientBaseCardState;
import it.polimi.ingsw.model.client.card.ClientCardState;
import it.polimi.ingsw.model.client.state.ClientModelState;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.state.VoyageState;

public class StardustState extends CardState {

	private final StardustCard card;
	private ArrayList<Player> awaiting = null;

	public StardustState(VoyageState state, StardustCard card) {
		super(state);
		if (card == null) throw new NullPointerException();
		this.card = card;
	}

	@Override
	public void init(ClientModelState new_state) {
		super.init(new_state);
		for (Player p : this.state.getOrder(CardOrder.INVERSE)) {
			card.apply(this.state, p);
		}
		this.awaiting = new ArrayList<>(state.getOrder(CardOrder.NORMAL));
	}

	@Override
	public void validate(ServerMessage message) throws ForbiddenCallException {
		message.receive(this);
		if (!awaiting.isEmpty()) {
			this.state.broadcastMessage(new NotifyStateUpdateMessage(this.state.getClientState()));
			return;
		}
		this.transition();
	}

	@Override
	public ClientCardState getClientCardState() {
		ArrayList<PlayerColor> tmp = new ArrayList<>(this.awaiting.stream().map(p -> p.getColor()).toList());
		return new ClientAwaitConfirmCardStateDecorator(
				new ClientBaseCardState(card.getId()),
				tmp);
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
	protected CardState getNext() {
		return null;
	}

	@Override
	public void disconnect(Player p) throws ForbiddenCallException {
		if (this.awaiting.contains(p)) {
			this.awaiting.remove(p);
		}
	}

}
