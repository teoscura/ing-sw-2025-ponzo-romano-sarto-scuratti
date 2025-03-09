package it.polimi.ingsw.board;

interface iPlanche {
	
	public int getPlayerPosition(PlayerColor c);

	public PlayerColor getPlayersAt(int Position);

	public void movePlayer(PlayerColor c, int rel_change);

	public PlayerColor won();

}
