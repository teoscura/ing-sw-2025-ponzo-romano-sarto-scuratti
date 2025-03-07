package it.polimi.ingsw.board;

public interface iPlanche {


	int getPlayerPosition(PlayerColor c);


	PlayerColor getPlayersAt(int Position);


	void movePlayer(PlayerColor c, int rel_change);


	PlayerColor won();


}
