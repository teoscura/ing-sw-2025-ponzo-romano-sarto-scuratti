package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.player.PlayerColor;


import java.util.EnumMap;
import java.util.Map;

public class Planche implements iPlanche {

	private final Map<PlayerColor, Integer> playerMoves;

	public Planche() {

		this.playerMoves = new EnumMap<>(PlayerColor.class);
		for (PlayerColor color : PlayerColor.values()) {
			playerMoves.put(color, 0);
		}
	}
	// TODO  initialise the position of players in the beginning. The player behind is at position NUM_OF_SPACES

	@Override
	public int getPlayerPosition(PlayerColor c) {
		return playerMoves.get(c) % NUM_OF_SPACES; //TODO find out how to get this from the type of game

	}

	@Override
	public PlayerColor getPlayersAt(int position) { //TODO refactor this (singular)

		for (Map.Entry<PlayerColor, Integer> player : playerMoves.entrySet()) {
			if (player.getValue() % NUM_OF_SPACES == position) {
				return player.getKey();
			}
		}
		return null;
	}

	@Override
	public void movePlayer(PlayerColor c, int rel_change) {
		// check if player was lapped and if two players are in the same position
		if (rel_change == 0) return;
		int exp_position = playerMoves.get(c) + rel_change;
		boolean different_positions = false;
		while (!different_positions) {
			for (Map.Entry<PlayerColor, Integer> p : playerMoves.entrySet()) {
				if (exp_position == p.getValue()) {
					if (rel_change > 0) {
						exp_position++;
					} else {
						exp_position--;
					}
				} else {
					different_positions = true;
				}
			}
		}


		playerMoves.put(c, exp_position);

		for (Map.Entry<PlayerColor, Integer> p1 : playerMoves.entrySet()) {
			for (Map.Entry<PlayerColor, Integer> p2 : playerMoves.entrySet()) {
				if (p1.getValue() + NUM_OF_SPACES <= p2.getValue()) {
					playerLost(p1.getKey()); //TODO make this method
				}
			}

		}
	}


	@Override
	public PlayerColor won() {
		return null; //TODO
	}


}
