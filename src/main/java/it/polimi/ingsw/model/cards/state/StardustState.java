package it.polimi.ingsw.model.cards.state;

import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.cards.StardustCard;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.cards.utils.CardOrder;
import it.polimi.ingsw.model.client.card.ClientAwaitConfirmCardStateDecorator;
import it.polimi.ingsw.model.client.card.ClientBaseCardState;
import it.polimi.ingsw.model.client.card.ClientCardState;
import it.polimi.ingsw.model.client.state.ClientState;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.state.VoyageState;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.LoggerLevel;

import java.util.ArrayList;

public class StardustState extends CardState {

	private final StardustCard card;
	private final ArrayList<Player> awaiting;

	public StardustState(VoyageState state, StardustCard card) {
		super(state);
		if (card == null) throw new NullPointerException();
		this.card = card;
		this.awaiting = new ArrayList<>(state.getOrder(CardOrder.NORMAL));
	}

	@Override
	public void init(ClientState new_state) {
		super.init(new_state);
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "New CardState -> Stardust State!");
		for (Player p : this.state.getOrder(CardOrder.NORMAL)) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + p.voyageInfo(this.state.getPlanche()));
		}
	}

	@Override
	public void validate(ServerMessage message) throws ForbiddenCallException {
		message.receive(this);
		if (!awaiting.isEmpty()) {
			this.state.broadcastMessage(new NotifyStateUpdateMessage(this.state.getClientState()));
			return;
		}
		for (Player p : this.state.getOrder(CardOrder.INVERSE)) {
			card.apply(this.state, p);
		}
		this.transition();
	}

	@Override
	public ClientCardState getClientCardState() {
		ArrayList<PlayerColor> tmp = new ArrayList<>(this.awaiting.stream().map(p -> p.getColor()).toList());
		return new ClientAwaitConfirmCardStateDecorator(
				new ClientBaseCardState(
						this.getClass().getSimpleName(),
						card.getId()),
				tmp);
	}

	@Override
	public void progressTurn(Player p) {
		if (!this.awaiting.contains(p)) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to progress the turn while already having done so!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to progress the turn while already having done so!"));
			return;
		}
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' motioned to progress!");
		this.awaiting.remove(p);
	}

	@Override
	public CardState getNext() {
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Card exhausted, moving to a new one!");
		return null;
	}

	@Override
	public void disconnect(Player p) throws ForbiddenCallException {
		this.awaiting.remove(p);
	}

}
