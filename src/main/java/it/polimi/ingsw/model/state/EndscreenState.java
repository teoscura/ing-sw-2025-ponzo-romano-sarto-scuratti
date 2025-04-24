package it.polimi.ingsw.model.state;

import java.util.ArrayList;

import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.adventure_cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.client.player.ClientEndgamePlayer;
import it.polimi.ingsw.model.client.state.ClientEndgameState;
import it.polimi.ingsw.model.client.state.ClientModelState;
import it.polimi.ingsw.model.player.Player;

public class EndscreenState extends GameState {

	private final ArrayList<Player> awaiting;
	private final ArrayList<Player> order_arrival;

	public EndscreenState(ModelInstance model, GameModeType type, PlayerCount count, ArrayList<Player> players, ArrayList<Player> order_arrival) {
		super(model, type, count, players);
		if (order_arrival == null) throw new NullPointerException();
		this.order_arrival = order_arrival;
		this.awaiting = new ArrayList<>();
		this.awaiting.addAll(this.players);
	}

	@Override
	public void init() {
		int min = Integer.MAX_VALUE;
		for (Player p : this.players) {
			if (p.getRetired()) continue;
			min = p.getSpaceShip().countExposedConnectors() <= min ? p.getSpaceShip().countExposedConnectors() : min;
		}
		for (Player p : this.players) {
			if (p.getRetired()) continue;
			if (p.getSpaceShip().countExposedConnectors() == min) p.addScore(2);
			if (this.order_arrival.contains(p)) p.addScore(4 - order_arrival.indexOf(p));
			p.finalScore();
		}
		super.init();
	}

	@Override
	public void validate(ServerMessage message) throws ForbiddenCallException {
		message.receive(this);
		if (!awaiting.isEmpty()) {
			this.broadcastMessage(new NotifyStateUpdateMessage(this.getClientState()));
			return;
		}
		this.transition();
	}

	@Override
	public GameState getNext() {
		return null;
	}

	@Override
	public ClientModelState getClientState() {
		ArrayList<ClientEndgamePlayer> tmp = new ArrayList<>();
		for (Player p : this.players) {
			tmp.add(new ClientEndgamePlayer(p.getUsername(),
					p.getColor(),
					this.order_arrival.indexOf(p),
					p.getCredits(),
					p.getSpaceShip().getContains(),
					p.getScore()));
		}
		return new ClientEndgameState(tmp);
	}

	@Override
	public boolean toSerialize() {
		return false;
	}


	@Override
	public void sendContinue(Player p) throws ForbiddenCallException {
		this.awaiting.remove(p);
		this.model.kick(p.getDescriptor());
	}

	@Override
	public void disconnect(Player p) {
		if (!this.awaiting.contains(p)) {
			return;
		}
		this.awaiting.remove(p);
	}

	@Override
	public String toString() {
		return "should never be called";
	}

}
