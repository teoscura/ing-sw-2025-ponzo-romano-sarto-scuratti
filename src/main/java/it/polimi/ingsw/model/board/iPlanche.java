package it.polimi.ingsw.model.board;

import java.io.Serializable;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.state.VoyageState;

public interface iPlanche extends Serializable {

	int getPlayerPosition(Player c);

	Player getPlayerAt(int Position);

	void movePlayer(VoyageState state, Player c, int rel_change);

	void loseGame(Player p);
}
