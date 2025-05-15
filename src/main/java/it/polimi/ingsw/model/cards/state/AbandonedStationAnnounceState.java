package it.polimi.ingsw.model.cards.state;

import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.cards.AbandonedStationCard;
import it.polimi.ingsw.model.cards.exceptions.CrewSizeException;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.client.card.ClientBaseCardState;
import it.polimi.ingsw.model.client.card.ClientCardState;
import it.polimi.ingsw.model.client.card.ClientLandingCardStateDecorator;
import it.polimi.ingsw.model.client.state.ClientState;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.state.VoyageState;

import java.util.ArrayList;
import java.util.List;

public class AbandonedStationAnnounceState extends CardState {

	private final AbandonedStationCard card;
	private final ArrayList<Player> list;
	private boolean responded = false;
	private int id = -1;

	public AbandonedStationAnnounceState(VoyageState state, AbandonedStationCard card, List<Player> list) {
		super(state);
		if (list.size() > this.state.getCount().getNumber() || list.size() < 1 || list == null)
			throw new IllegalArgumentException("Constructed insatisfyable state");
		if (card == null) throw new NullPointerException();
		this.card = card;
		this.list = new ArrayList<>(list);
	}

	@Override
	public void init(ClientState new_state) {
		super.init(new_state);
		System.out.println("New CardState -> Abandoned Station Announce State!");
		for (Player p : this.list) {
			System.out.println("	 - " + p.getUsername());
		}
	}

	@Override
	public void validate(ServerMessage message) throws ForbiddenCallException {
		message.receive(this);
		if (!responded) {
			this.state.broadcastMessage(new NotifyStateUpdateMessage(this.state.getClientState()));
			return;
		}
		try {
			this.card.apply(state, this.list.getFirst(), id);
		} catch (CrewSizeException e) {
			System.out.println("Player '" + this.list.getFirst().getUsername() + "' attempted to land with not enough crew!");
			this.state.broadcastMessage(new ViewMessage("Player'" + this.list.getFirst().getUsername() + "' attempted to land with not enough crew!"));
			return;
		}
		this.transition();
	}

	@Override
	public ClientCardState getClientCardState() {
		ArrayList<Boolean> tmp = new ArrayList<>(List.of(true));
		return new ClientLandingCardStateDecorator(new ClientBaseCardState(this.card.getId()),
				this.list.getFirst().getColor(),
				this.card.getDays(),
				this.card.getCrewLost(),
				tmp);
	}

	@Override
	public CardState getNext() {
		if (this.card.getExhausted()) return new AbandonedStationRewardState(state, card, list);
		this.list.removeFirst();
		if (!this.list.isEmpty()) return new AbandonedStationAnnounceState(state, card, list);
		System.out.println("Card exhausted, moving to a new one!");
		return null;
	}

	@Override
	public void selectLanding(Player p, int planet) {
		if (p != this.list.getFirst()) {
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
	}

	@Override
	public void disconnect(Player p) throws ForbiddenCallException {
		if (this.list.getFirst() == p) {
			this.responded = true;
			this.id = -1;
			return;
		}
		this.list.remove(p);
	}

}
