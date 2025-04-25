package it.polimi.ingsw.model.cards.state;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.cards.AbandonedShipCard;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.client.card.ClientBaseCardState;
import it.polimi.ingsw.model.client.card.ClientCardState;
import it.polimi.ingsw.model.client.card.ClientLandingCardStateDecorator;
import it.polimi.ingsw.model.client.state.ClientModelState;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.state.VoyageState;

public class AbandonedShipAnnounceState extends CardState {

	private final AbandonedShipCard card;
	private final ArrayList<Player> list;
	private boolean responded = false;
	private int id = -1;

	public AbandonedShipAnnounceState(VoyageState state, AbandonedShipCard card, List<Player> list) {
		super(state);
		if (list.size() > this.state.getCount().getNumber() || list.size() < 1 || list == null)
			throw new IllegalArgumentException("Constructed insatisfyable state");
		if (card == null) throw new NullPointerException();
		this.card = card;
		this.list = new ArrayList<>(list);
	}

	@Override
	public void init(ClientModelState new_state) {
		super.init(new_state);
		System.out.println("New CardState -> Abandoned Ship Announce State!");
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
		try{
			this.card.apply(state, this.list.getFirst(), id);
		} catch (IllegalArgumentException e){
			System.out.println("Player '" + this.list.getFirst().getUsername() + "' attempted to land without enough crew!");
			this.state.broadcastMessage(new ViewMessage("Player'" + this.list.getFirst().getUsername() + "' attempted to land without enough crew!"));
			this.responded = false;
			return;
		}
		this.transition();
	}

	@Override
	public ClientCardState getClientCardState() {
		ArrayList<Boolean> tmp = new ArrayList<>(Arrays.asList(true));
		return new ClientLandingCardStateDecorator(new ClientBaseCardState(this.card.getId()),
				this.list.getFirst().getColor(),
				this.card.getDays(),
				this.card.getCrewLost(),
				tmp);
	}

	@Override
    public CardState getNext() {
		if (this.list.getFirst().getDisconnected()){
			this.list.removeFirst();
			return new AbandonedShipAnnounceState(state, card, list);
		}
		if (this.card.getExhausted()) return new AbandonedShipRewardState(state, card, list);
		this.list.removeFirst();
		if (!this.list.isEmpty()) return new AbandonedShipAnnounceState(state, card, list);
		System.out.println("...Card exhausted, moving to a new one!");
		return null;
	}

	@Override
	public void selectLanding(Player p, int planet) {
		if (!p.equals(this.list.getFirst())) {
			System.out.println("Player '" + p.getUsername() + "' attempted to land during another player's turn!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to land during another player's turn!"));
			return;
		} else if (planet != -1 && planet != 0) {
			System.out.println("Player '" + p.getUsername() + "' attempted to land on an invalid id!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to land on an invalid id!"));
			return;
		}
		this.id = planet;
		this.responded = true;
		System.out.println("Player '" + p.getUsername() + "' landed on id: "+this.id+".");
	}

	@Override
	public void disconnect(Player p) throws ForbiddenCallException {
		if (this.list.getFirst().equals(p)) {
			this.responded = true;
			this.id = -1;
		}
		else if (this.list.contains(p)) {
			this.list.remove(p);
		}
		System.out.println("Player '" + p.getUsername() + "' disconnected!");
	}

}
