package it.polimi.ingsw.model.cards.state;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.exceptions.PlayerNotFoundException;
import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.cards.EpidemicCard;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.cards.utils.CardOrder;
import it.polimi.ingsw.model.client.card.ClientAwaitConfirmCardStateDecorator;
import it.polimi.ingsw.model.client.card.ClientBaseCardState;
import it.polimi.ingsw.model.client.card.ClientCardState;
import it.polimi.ingsw.model.client.state.ClientModelState;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.state.VoyageState;

public class EpidemicState extends CardState {

	private final EpidemicCard card;
	private final ArrayList<Player> awaiting;

	public EpidemicState(VoyageState state, EpidemicCard card) {
		super(state);
		if (card == null) throw new NullPointerException();
		this.card = card;
		this.awaiting = new ArrayList<>(state.getOrder(CardOrder.NORMAL));
	}

	@Override
	public void init(ClientModelState new_state) {
		super.init(new_state);
		for (Player p : this.state.getOrder(CardOrder.INVERSE)) {
			try {
				card.apply(this.state, p);
				if (p.getSpaceShip().getCrew()[0] == 0) this.state.loseGame(p);
			} catch (PlayerNotFoundException e) {
				e.printStackTrace();
			}
		}
		System.out.println("New CardState -> Epidemic State!");
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
		this.transition();
	}

	@Override
	public ClientCardState getClientCardState() {
		List<PlayerColor> tmp = this.awaiting.stream().map(p -> p.getColor()).toList();
		return new ClientAwaitConfirmCardStateDecorator(
				new ClientBaseCardState(card.getId()),
				new ArrayList<>(tmp));
	}

	@Override
	public void progressTurn(Player p) {
		if (!this.awaiting.contains(p)) {
			System.out.println("Player '" + p.getUsername() + "' attempted to progress the turn while already having done so!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to progress the turn while already having done so!"));
			return;
		}
		this.awaiting.remove(p);
		System.out.println("Player '" + p.getUsername() + "' motioned to progress! ("+(this.state.getCount().getNumber()-this.awaiting.size())+").");
	}

	@Override
    public CardState getNext() {
		System.out.println("Card exhausted, moving to a new one!");
		return null;
	}

	@Override
	public void disconnect(Player p) throws ForbiddenCallException {
		if (this.awaiting.contains(p)) {
			this.awaiting.remove(p);
		}
		System.out.println("Player '" + p.getUsername() + "' disconnected!");
	}

}
