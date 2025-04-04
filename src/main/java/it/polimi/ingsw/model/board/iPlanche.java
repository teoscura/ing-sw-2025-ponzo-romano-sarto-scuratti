package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.player.Player;

public interface iPlanche {

	public int getPlayerPosition(Player c);

	public Player getPlayerAt(int Position);

	public void movePlayer(Player c, int rel_change);
}
