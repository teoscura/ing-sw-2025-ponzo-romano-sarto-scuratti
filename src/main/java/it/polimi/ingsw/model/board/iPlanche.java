package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.state.VoyageState;

public interface iPlanche {

	public int getPlayerPosition(Player c);
	public Player getPlayerAt(int Position);
	public void movePlayer(VoyageState state, Player c, int rel_change);
	public void loseGame(Player p);
}
