package it.polimi.ingsw.model.cards.state;

import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.cards.AbandonedShipCard;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.cards.visitors.CrewRemoveVisitor;
import it.polimi.ingsw.model.client.card.ClientBaseCardState;
import it.polimi.ingsw.model.client.card.ClientCardState;
import it.polimi.ingsw.model.client.card.ClientCrewPenaltyCardStateDecorator;
import it.polimi.ingsw.model.client.state.ClientState;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.VoyageState;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.LoggerLevel;

import java.util.ArrayList;
import java.util.List;

public class AbandonedShipRewardState extends CardState {

	private final AbandonedShipCard card;
	private final ArrayList<Player> list;
	private int done = 0;
	private boolean responded;

	public AbandonedShipRewardState(VoyageState state, AbandonedShipCard card, List<Player> list) {
		super(state);
		if (state == null || list == null || card == null)
			throw new IllegalArgumentException("Constructed insatisfyable state");
		this.list = new ArrayList<>(list);
		this.card = card;
	}

	@Override
	public void init(ClientState new_state) {
		super.init(new_state);
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "CardState -> Abandoned Ship Reward State!");
		for (Player p : this.list) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + p.voyageInfo(this.state.getPlanche()));
		}
	}

	@Override
	public void validate(ServerMessage message) throws ForbiddenCallException {
		message.receive(this);
		if (!responded && !this.list.getFirst().getRetired()) {
			this.state.broadcastMessage(new NotifyStateUpdateMessage(this.state.getClientState()));
			return;
		}
		this.list.getFirst().giveCredits(this.card.getCredits());
		this.state.getPlanche().movePlayer(state, list.getFirst(), -card.getDays());
		this.transition();
	}

	@Override
	public ClientCardState getClientCardState() {
		return new ClientCrewPenaltyCardStateDecorator(new ClientBaseCardState(
					this.getClass().getSimpleName(),				
					this.card.getId()),
				this.list.getFirst().getColor(),
				this.card.getCrewLost() - this.done);
	}

	@Override
	public CardState getNext() {
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Card exhausted, moving to a new one!");
		return null;
	}

	@Override
	public void removeCrew(Player p, ShipCoords cabin_coords) throws ForbiddenCallException {
		if (p != this.list.getFirst()) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to remove the crew during another player's turn!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to remove the crew during another player's turn!"));
			return;
		}
		CrewRemoveVisitor v = new CrewRemoveVisitor(p.getSpaceShip());
		try {
			p.getSpaceShip().getComponent(cabin_coords).check(v);
		} catch (IllegalTargetException e) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to remove the crew from a invalid coordinate!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to remove the crew from a invalid coordinate!"));
			return;
		}
		if (p.getSpaceShip().getCrew()[0] == 0) {
			this.state.loseGame(p);
			return;
		}
		this.done++;
		if (this.done >= this.card.getCrewLost()) {
			this.responded = true;
		}
	}

	@Override
	public void disconnect(Player p) throws ForbiddenCallException {
		if (this.list.getFirst() == p) this.transition();
	}

}
