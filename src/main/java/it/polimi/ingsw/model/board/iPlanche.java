package it.polimi.ingsw.model.board;

import java.util.List;

import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.iSpaceShip;

public interface iPlanche {

	public int getPlayerPosition(iSpaceShip ship);

	public PlayerColor getPlayersAt(int Position);

	public void movePlayer(iSpaceShip ship, int rel_change);

	public List<PlayerColor> getOrder();
}
