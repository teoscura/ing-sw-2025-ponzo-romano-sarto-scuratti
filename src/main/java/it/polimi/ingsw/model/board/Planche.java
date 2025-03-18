package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.iSpaceShip;

import java.util.*;

public class Planche implements iPlanche {

	private final int board_length;
	private final Map<iSpaceShip, Integer> playerMoves;
	List<PlayerColor> Sorted_Players = new ArrayList<>();

	public Planche() {

		this.playerMoves = new EnumMap<>(PlayerColor.class);
		for (PlayerColor color : PlayerColor.values()) {
			playerMoves.put(color, 0);
		}
	}
	// TODO initialise the position of players in the beginning. The player behind
	// is at position board_length

	@Override
	public int getPlayerPosition(iSpaceShip c) {
		return playerMoves.get(c) % board_length; // TODO find out how to get this from the type of game
	}

	@Override
	public PlayerColor getPlayersAt(int position) { // TODO refactor this (singular)

		for (Map.Entry<PlayerColor, Integer> player : playerMoves.entrySet()) {
			if (player.getValue() % board_length == position) {
				return player.getKey();
			}
		}
		return null;
	}

	@Override
	public void movePlayer(PlayerColor c, int rel_change) {
		// check if player was lapped and if two players are in the same position
		if (rel_change == 0)
			return;
		int exp_position = playerMoves.get(c) + rel_change;

		for (Map.Entry<PlayerColor, Integer> p : playerMoves.entrySet()) {
			if (exp_position == p.getValue()) {
				if (rel_change > 0) {
					exp_position++;
				} else {
					exp_position--;
				}
			} else {
				playerMoves.put(c, exp_position);
				return;
			}
		}

		playerMoves.put(c, exp_position);
		return;

	}

	@Override
	public boolean checkLapped() {
		for (Map.Entry<iSpaceShip, Integer> p1 : playerMoves.entrySet()) {
			for (Map.Entry<PlayerColor, Integer> p2 : playerMoves.entrySet()) {
				if (p1.getValue() + board_length <= p2.getValue()) {
					return true;
				}
			}
		}
		return false;
	}

	public PlayerColor getFirstPlayer() {
		Map.Entry<PlayerColor, Integer> max = null;
		for (Map.Entry<PlayerColor, Integer> p : playerMoves.entrySet()) {
			if (max == null || p.getValue() > max.getValue()) {
				max = p;
			}
		}
		return max.getKey();
	}

	public PlayerColor getNextPlayer(PlayerColor previousPlayer) {
		Map.Entry<PlayerColor, Integer> max = null;
		for (Map.Entry<PlayerColor, Integer> p : playerMoves.entrySet()) {
			if ((max == null || p.getValue() > max.getValue()) && p.getValue() < playerMoves.get(previousPlayer)) {
				max = p;
			}
		}
		if(max == null){
			return null;
		}
		return max.getKey();
	}

}
