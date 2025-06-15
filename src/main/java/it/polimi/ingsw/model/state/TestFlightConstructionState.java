package it.polimi.ingsw.model.state;

import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.board.TestFlightCards;
import it.polimi.ingsw.model.client.player.ClientConstructionPlayer;
import it.polimi.ingsw.model.client.state.ClientConstructionState;
import it.polimi.ingsw.model.client.state.ClientState;
import it.polimi.ingsw.model.player.Player;

import java.util.ArrayList;

/**
 * Represents a the construction phase of a Galaxy Trucker match, using test flight rules.
 */
public class TestFlightConstructionState extends ConstructionState {

	/**
	 * Constructs a {@link ConstructionState} object.
	 * 
	 * @param model {@link it.polimi.ingsw.model.ModelInstance} ModelInstance that owns this {{@link it.polimi.ingsw.model.state.GameState}.
	 * @param type {@link GameModeType} Ruleset of the state.
	 * @param count {@link it.polimi.ingsw.model.PlayerCount} Size of the match.
	 * @param players Array of all {@link it.polimi.ingsw.model.player.Player players} in the match.
	 */
	public TestFlightConstructionState(ModelInstance model, GameModeType type, PlayerCount count, ArrayList<Player> players) {
		super(model, type, count, players, new TestFlightCards());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ClientState getClientState() {
		ArrayList<ClientConstructionPlayer> tmp = new ArrayList<>();
		ArrayList<Integer> discarded = new ArrayList<>(this.board.getDiscarded());
		for (Player p : this.players) {
			ArrayList<Integer> stash = new ArrayList<>();
			int id = this.current_tile.get(p) == null ? -1 : this.current_tile.get(p).getID();
			stash.addAll(this.hoarded_tile.get(p).stream().filter(t -> t != null).map(t -> t.getID()).toList());
			tmp.add(new ClientConstructionPlayer(p.getUsername(),
					p.getColor(),
					p.getSpaceShip().getClientSpaceShip(),
					id,
					stash,
					this.finished.contains(p),
					p.getDisconnected()));
		}
		return new ClientConstructionState(this.type, tmp, null, discarded, this.board.getCoveredSize(), 0, 0, null, null);
	}

}
