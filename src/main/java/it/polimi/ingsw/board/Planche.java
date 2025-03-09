package it.polimi.ingsw.board;

public class Planche implements iPlanche {

	private PlancheCell[] cells;

	@Override
	public int getPlayerPosition(PlayerColor c) {
		return 0; // TODO

	}

	@Override
	public PlayerColor getPlayersAt(int position) {
		return null; // TODO
	}

	@Override
	public void movePlayer(PlayerColor c, int rel_change) {
		return;
	}

	@Override
	public PlayerColor won() {
		return null; //TODO
	}


}
