package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.player.PlayerColor;

import java.util.*;
import java.util.Map.Entry;

public class Planche implements iPlanche {

	private final HashMap<PlayerColor, Integer> planche;
	private final int length;

	public Planche(GameModeType type, PlayerCount count){
		this.length = type.getLength();
		this.planche = new HashMap<PlayerColor, Integer>();
		int i = 0;
		for(PlayerColor p : PlayerColor.values()){
			if(i==count.getNumber()) break;
			planche.put(p, this.length);
			i++;
		}
	}

	@Override
	public int getPlayerPosition(PlayerColor c) {
		return this.planche.get(c) - this.length;
	}

	@Override
	public PlayerColor getPlayersAt(int position) {
		int cell = position % this.length;
		for(Entry<PlayerColor, Integer> p : planche.entrySet()){
			if(p.getValue()%this.length==cell) return p.getKey();
		}
		return PlayerColor.NONE;
	}

	@Override
	public void movePlayer(PlayerColor c, int rel_change){
		if(!this.planche.containsKey(c)) throw new IllegalArgumentException("Color is not present in the current game.");
		int position = this.planche.get(c);
		int count = rel_change > 0 ? rel_change : -rel_change;
		while(count>0){
			position += rel_change>=0 ? 1 : -1;
			if(this.getPlayersAt(position)!=PlayerColor.NONE) continue;
			count--;
		}
		this.planche.put(c, position);
	}

	@Override
	public List<PlayerColor> getOrder(){
		return this.planche.entrySet().stream().sorted().map(m -> m.getKey()).toList();
	}
}
