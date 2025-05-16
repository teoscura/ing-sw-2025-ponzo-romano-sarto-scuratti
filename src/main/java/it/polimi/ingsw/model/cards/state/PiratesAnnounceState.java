package it.polimi.ingsw.model.cards.state;

import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.cards.PiratesCard;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.client.card.ClientAwaitConfirmCardStateDecorator;
import it.polimi.ingsw.model.client.card.ClientBaseCardState;
import it.polimi.ingsw.model.client.card.ClientCardState;
import it.polimi.ingsw.model.client.state.ClientState;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.VoyageState;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.LoggerLevel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PiratesAnnounceState extends CardState {

	private final PiratesCard card;
	private final ArrayList<Player> list;
	private boolean responded = false;
	private boolean result = true;

	public PiratesAnnounceState(VoyageState state, PiratesCard card, List<Player> list) {
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
		if (list.size() == this.state.getCount().getNumber())
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "New CardState -> Pirates Announce State!");
		else
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "CardState -> Pirates Announce State!");
		for (Player p : this.list) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + p.voyageInfo(this.state.getPlanche()));
		}
	}

	@Override
	public void validate(ServerMessage message) throws ForbiddenCallException {
		message.receive(this);
		if (!responded) {
			this.state.broadcastMessage(new NotifyStateUpdateMessage(this.state.getClientState()));
			return;
		}
		if (!this.list.getFirst().getDisconnected()) result = this.card.apply(this.state, this.list.getFirst());
		this.transition();
	}

	@Override
	public ClientCardState getClientCardState() {
		List<PlayerColor> awaiting = Collections.singletonList(this.list.getFirst().getColor());
		return new ClientAwaitConfirmCardStateDecorator(new ClientBaseCardState(this.card.getId()), new ArrayList<>(awaiting));
	}

	@Override
	public CardState getNext() {
		if (this.list.getFirst().getDisconnected()) {
			this.list.removeFirst();
			if (!this.list.isEmpty()) return new PiratesAnnounceState(state, card, list);
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Card exhausted, moving to a new one!");
			return null;
		}
		if (!result) return new PiratesPenaltyState(state, card, list, card.getShots());
		if (this.card.getExhausted()) return new PiratesRewardState(state, card, list);
		this.list.removeFirst();
		if (!this.list.isEmpty()) return new PiratesAnnounceState(state, card, list);
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Card exhausted, moving to a new one!");
		return null;
	}

	@Override
	public void turnOn(Player p, ShipCoords target_coords, ShipCoords battery_coords) {
		if (!this.list.getFirst().equals(p)) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to turn on a component during another player's turn!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to turn on a component during another player's turn!"));
			return;
		}
		try {
			p.getSpaceShip().turnOn(target_coords, battery_coords);
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' turned on component at" + target_coords + " using battery from " + battery_coords + "!");
		} catch (IllegalTargetException e) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to turn on a component with invalid coordinates!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to turn on a component with invalid coordinates!"));
		}
	}

	@Override
	public void progressTurn(Player p) {
		if (!this.list.getFirst().equals(p)) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to progress during another player's turn!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to progress during another player's turn!"));
			return;
		}
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' motioned to progress!");
		this.responded = true;
	}

	@Override
	public void disconnect(Player p) throws ForbiddenCallException {
		if (this.list.getFirst().equals(p)) {

			this.responded = true;
			return;
		}
		this.list.remove(p);

	}

}
