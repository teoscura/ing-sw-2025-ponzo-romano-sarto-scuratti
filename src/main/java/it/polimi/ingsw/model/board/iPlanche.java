package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.iSpaceShip;

interface iPlanche {

	public int getPlayerPosition(iSpaceShip c);

	public PlayerColor getPlayersAt(int Position);

	public void movePlayer(iSpaceShip c, int rel_change);

}
