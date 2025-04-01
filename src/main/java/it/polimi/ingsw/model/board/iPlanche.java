package it.polimi.ingsw.model.board;

import java.util.List;

import it.polimi.ingsw.model.player.PlayerColor;

public interface iPlanche {

	public int getPlayerPosition(PlayerColor c);

	public PlayerColor getPlayersAt(int Position);

	public void movePlayer(PlayerColor c, int rel_change);

	public List<PlayerColor> getOrder();
}
