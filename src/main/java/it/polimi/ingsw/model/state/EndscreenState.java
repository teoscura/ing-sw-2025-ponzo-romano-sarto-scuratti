package it.polimi.ingsw.model.state;

import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.client.ClientGameListEntry;
import it.polimi.ingsw.model.client.player.ClientEndgamePlayer;
import it.polimi.ingsw.model.client.state.ClientEndgameState;
import it.polimi.ingsw.model.client.state.ClientState;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.LoggerLevel;

import java.util.ArrayList;

/**
 * Ending point of any completed Galaxy Trucker match, displays all scores and stats for all playing {@link it.polimi.ingsw.model.player.Player players}.
 */
public class EndscreenState extends GameState {

	private final ArrayList<Player> awaiting;
	private final ArrayList<Player> order_arrival;

	/**
	 * Constructs a {@link EndscreenState} object.
	 *
	 * @param model {@link it.polimi.ingsw.model.ModelInstance} ModelInstance that owns this {{@link it.polimi.ingsw.model.state.GameState}.
	 * @param type {@link GameModeType} Ruleset of the state.
	 * @param count {@link it.polimi.ingsw.model.PlayerCount} Size of the match.
	 * @param players Array of all {@link it.polimi.ingsw.model.player.Player players} in the match.
	 * @param order_arrival Array of all {@link it.polimi.ingsw.model.player.Player players} that were alive at the end of the match, in descending order of distance.
	 */
	public EndscreenState(ModelInstance model, GameModeType type, PlayerCount count, ArrayList<Player> players, ArrayList<Player> order_arrival) {
		super(model, type, count, players);
		if (order_arrival == null) throw new NullPointerException();
		this.order_arrival = order_arrival;
		this.awaiting = new ArrayList<>();
		var await = players.stream().filter(p->!p.getDisconnected()).toList();
		this.awaiting.addAll(await);
	}

	/**
	 * Finalizes scores for all {@link it.polimi.ingsw.model.player.Player}.
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void init() {
		int min = Integer.MAX_VALUE;
		for (Player p : this.players) {
			if (p.getRetired()) continue;
			min = p.getSpaceShip().countExposedConnectors() <= min ? p.getSpaceShip().countExposedConnectors() : min;
		}
		for (Player p : this.players) {
			if (p.getRetired()) continue;
			if (p.getSpaceShip().countExposedConnectors() == min) p.giveCredits(2);
			if (this.order_arrival.contains(p)) p.giveCredits(4 - order_arrival.indexOf(p));
			p.finalScore();
		}
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "New Game State -> Endscreen State");
		this.broadcastMessage(new NotifyStateUpdateMessage(this.getClientState()));
	}

	/**
	 * Verifies that all players have motioned to progress, and if so, closes the match.
	 * 
	 * {@inheritDoc}.
	 */
	@Override
	public void validate(ServerMessage message) throws ForbiddenCallException {
		message.receive(this);
		if (!awaiting.isEmpty()) {
			this.broadcastMessage(new NotifyStateUpdateMessage(this.getClientState()));
			return;
		}
		this.transition();
	}

	/**
	 * Always returns {@code null}.
	 */
	@Override
	public GameState getNext() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ClientState getClientState() {
		ArrayList<ClientEndgamePlayer> tmp = new ArrayList<>();
		for (Player p : this.players) {
			tmp.add(new ClientEndgamePlayer(p.getUsername(),
					p.getColor(),
					this.order_arrival.indexOf(p),
					p.getCredits(),
					p.getSpaceShip().getContains()));
		}
		return new ClientEndgameState(tmp, this.awaiting.stream().map(p->p.getColor()).toList());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean toSerialize() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendContinue(Player p) throws ForbiddenCallException {
		this.awaiting.remove(p);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void disconnect(Player p) {
		if (!this.awaiting.contains(p)) {
			return;
		}
		this.awaiting.remove(p);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Endscreen State";
	}

	/**
	 * {@inheritDoc}
	 */
	public ClientGameListEntry getOngoingEntry(int id) {
		return new ClientGameListEntry(type, count, this.toString(), this.players.stream().map(p -> p.getUsername()).toList(), id);
	}

}
