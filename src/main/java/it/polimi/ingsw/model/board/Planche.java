package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.state.VoyageState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

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

	@Override
	public int getPlayerPosition(Player p) {
		if (!this.planche.containsKey(p)) return -1;
		return this.planche.get(p);
	}

	@Override
	public Player getPlayerAt(int position) {
		int cell = position % this.length;
		for (Entry<Player, Integer> p : planche.entrySet()) {
			if (p.getValue() % this.length == cell) return p.getKey();
		}
		return null;
	}

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
		// if (rel_change > 0) {
		// 	ArrayList<Player> to_lose = new ArrayList<>();
		// 	for (Player other : this.planche.keySet()) {
		// 		if (p.equals(other)) continue;
		// 		if (this.planche.get(p) - this.planche.get(other) >= this.length) {
		// 			to_lose.add(other);
		// 		}
		// 	}
		// 	for (Player l : to_lose) {
		// 		state.loseGame(l);
		// 	}
		// } else {
		// 	for (Player other : this.planche.keySet()) {
		// 		if (p.equals(other)) continue;
		// 		if (this.planche.get(other) - this.planche.get(p) >= this.length) {
		// 			state.loseGame(p);
		// 			return;
		// 		}
		// 	}
		// }

	}

	@Override
	public void loseGame(Player p) {
		this.planche.remove(p);
	}

}

