package it.polimi.ingsw.model.state;

import java.util.ArrayList;

import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.board.TestFlightCards;
import it.polimi.ingsw.model.client.player.ClientConstructionPlayer;
import it.polimi.ingsw.model.client.state.ClientConstructionState;
import it.polimi.ingsw.model.client.state.ClientModelState;
import it.polimi.ingsw.model.player.Player;

public class TestFlightConstructionState extends ConstructionState {

    public TestFlightConstructionState(ModelInstance model, GameModeType type, PlayerCount count, ArrayList<Player> players) {
        super(model, type, count, players, new TestFlightCards());
    }

    @Override
    public ClientModelState getClientState() {
		ArrayList<ClientConstructionPlayer> tmp = new ArrayList<>();
		ArrayList<Integer> discarded = new ArrayList<>(this.board.getDiscarded());
		for (Player p : this.players) {
			ArrayList<Integer> stash = new ArrayList<>();
			if (this.current_tile.get(p) != null) stash.add(this.current_tile.get(p).getID());
			stash.addAll(this.hoarded_tile.get(p).stream().filter(t -> t != null).map(t -> t.getID()).toList());
			tmp.add(new ClientConstructionPlayer(p.getUsername(),
					p.getColor(),
					p.getSpaceShip().getClientSpaceShip(),
					stash,
					this.finished.contains(p)));
		}
		return new ClientConstructionState(this.type, tmp, null, discarded, this.board.getCoveredSize(), null);
	}
    
}
