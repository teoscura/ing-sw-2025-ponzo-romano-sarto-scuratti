package it.polimi.ingsw.model.cards.state;

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
import it.polimi.ingsw.model.client.state.ClientState;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.state.VoyageState;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.LoggerLevel;

import java.util.ArrayList;
import java.util.List;

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
	public void init(ClientState new_state) {
		super.init(new_state);
		Logger.getInstance().print(LoggerLevel.MODEL, "["+state.getModelID()+"] "+"New CardState -> Epidemic State!");
		for (Player p : this.state.getOrder(CardOrder.NORMAL)) {
			Logger.getInstance().print(LoggerLevel.MODEL, "["+state.getModelID()+"] "+p.voyageInfo(this.state.getPlanche()));
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
			try {
				card.apply(this.state, p);
				if (p.getSpaceShip().getCrew()[0] == 0) this.state.loseGame(p);
			} catch (PlayerNotFoundException e) {
				e.printStackTrace();
			}
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
			Logger.getInstance().print(LoggerLevel.MODEL, "["+state.getModelID()+"] "+"Player: '" + p.getUsername() + "' attempted to progress the turn while already having done so!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to progress the turn while already having done so!"));
			return;
		}
		this.awaiting.remove(p);
		Logger.getInstance().print(LoggerLevel.MODEL, "["+state.getModelID()+"] "+"Player: '" + p.getUsername() + "' motioned to progress! (" + this.awaiting.size() + " missing).");
	}

	@Override
	public CardState getNext() {
		Logger.getInstance().print(LoggerLevel.MODEL, "["+state.getModelID()+"] "+"Card exhausted, moving to a new one!");
		return null;
	}

	@Override
	public void disconnect(Player p) throws ForbiddenCallException {
		this.awaiting.remove(p);

	}

}
