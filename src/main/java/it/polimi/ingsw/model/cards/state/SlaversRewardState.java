package it.polimi.ingsw.model.cards.state;

import java.util.ArrayList;

import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.cards.SlaversCard;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.client.card.ClientBaseCardState;
import it.polimi.ingsw.model.client.card.ClientCardState;
import it.polimi.ingsw.model.client.card.ClientCreditsRewardCardStateDecorator;
import it.polimi.ingsw.model.client.state.ClientModelState;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.state.VoyageState;

class SlaversRewardState extends CardState {

	private final SlaversCard card;
	private final ArrayList<Player> list;
	private boolean responded = false;
	private boolean took_reward = false;

	public SlaversRewardState(VoyageState state, SlaversCard card, ArrayList<Player> list) {
		super(state);
		if (list.size() > this.state.getCount().getNumber() || list.size() < 1 || list == null)
			throw new IllegalArgumentException("Constructed insatisfyable state");
		if (card == null) throw new NullPointerException();
		this.card = card;
		this.list = list;
	}

	@Override
	public void init(ClientModelState new_state) {
		super.init(new_state);
		System.out.println("    CardState -> Slavers Reward State!");
		for(Player p : this.list){
			System.out.println("	 - "+p.getUsername());
		}
	}

	@Override
	public void validate(ServerMessage message) throws ForbiddenCallException {
		message.receive(this);
		if (!responded) {
			this.state.broadcastMessage(new NotifyStateUpdateMessage(this.state.getClientState()));
			return;
		}
		if (took_reward) {
			this.list.getFirst().giveCredits(card.getCredits());
			this.state.getPlanche().movePlayer(state, list.getFirst(), -card.getDays());
		}
		this.transition();
	}

	@Override
	public ClientCardState getClientCardState() {
		return new ClientCreditsRewardCardStateDecorator(
				new ClientBaseCardState(this.card.getId()),
				this.list.getFirst().getColor(),
				this.card.getCredits(),
				this.card.getDays());
	}

	@Override
    public CardState getNext() {
		System.out.println("Card exhausted, moving to a new one!");
		return null;
	}

	@Override
	public void setTakeReward(Player p, boolean take) {
		if (p != this.list.getFirst()) {
			System.out.println("Player '" + p.getUsername() + "' attempted to accept a reward during another player's turn!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to accept a reward during another player's turn!"));
			return;
		}
		System.out.println("Player '"+p.getUsername()+"' took the reward? "+take);
		this.took_reward = take;
		this.responded = true;
	}

	@Override
	public void disconnect(Player p) throws ForbiddenCallException {
		if (this.list.getFirst() == p) {
			this.responded = true;
			this.took_reward = false;
		}
		System.out.println("Player '" + p.getUsername() + "' disconnected!");
	}

}
