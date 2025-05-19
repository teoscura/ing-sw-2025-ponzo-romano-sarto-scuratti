package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.state.VoyageState;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 * The planche class handles the movement of the player on the board.
 * Contains a HashMap that uses player color as a key, and the total steps taken by that player as a value.
 */
public class Planche implements iPlanche {

	private final HashMap<Player, Integer> planche;
	private final int length;

	public Planche(GameModeType type, List<Player> order) {
		this.length = type.getLength();
		this.planche = new HashMap<Player, Integer>();
		int i = 0;
		for (Player p : order) {
			planche.put(p, this.length + type.getStartingPos()[i]);
			i++;
		}
	}

	/**
	 * Returns the current position of the player
	 *
	 * @param p The Player
	 */
	@Override
	public int getPlayerPosition(Player p) {
		if (!this.planche.containsKey(p)) return -1;
		return this.planche.get(p);
	}

	/**
	 * Given a certain number of steps, finds if there are any players at the corrisponding cell on the board
	 *
	 * @param position
	 */
	@Override
	public Player getPlayerAt(int position) {
		int cell = position % this.length;
		for (Entry<Player, Integer> p : planche.entrySet()) {
			if (p.getValue() % this.length == cell) return p.getKey();
		}
		return null;
	}

	/**
	 * either moves the player forward or backwards by a number of steps equal to rel_change,
	 * if a player is already present on a cell, moves one more step forward of backwards
	 *
	 * @param state The Current State
	 * @param p The Player
	 * @param rel_change
	 */
	@Override
	public void movePlayer(VoyageState state, Player p, int rel_change) {
		if (!this.planche.containsKey(p))
			throw new IllegalArgumentException("Color is not present in the current game.");
		int position = this.planche.get(p);
		int count = rel_change > 0 ? rel_change : -rel_change;
		while (count > 0) {
			position += rel_change >= 0 ? 1 : -1;
			if (this.getPlayerAt(position) != null) continue;
			count--;
		}
		this.planche.put(p, position);
	}

	public boolean checkLapped(Player p) {
		return this.planche.keySet().stream().filter(pl -> getPlayerPosition(pl) - getPlayerPosition(p) >= length).findAny().isPresent();
	}

	@Override
	public void loseGame(Player p) {
		this.planche.remove(p);
	}

}

