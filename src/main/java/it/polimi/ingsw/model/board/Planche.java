package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.state.VoyageState;

import java.util.*;
import java.util.Map.Entry;

public class Planche implements iPlanche {

	private final HashMap<Player, Integer> planche;
	private final int length;

	public Planche(GameModeType type, List<Player> order){
		this.length = type.getLength();
		this.planche = new HashMap<Player, Integer>();
		int i = 0;
		for(Player p : order){
			planche.put(p, this.length + type.getStartingPos()[i]);
			i++;
		}
	}

	@Override
	public int getPlayerPosition(Player p) {
		return this.planche.get(p);
	}

	@Override
	public Player getPlayerAt(int position) {
		int cell = position % this.length;
		for(Entry<Player, Integer> p : planche.entrySet()){
			if(p.getValue()%this.length==cell) return p.getKey();
		}
		return null;
	}

	@Override
	public void movePlayer(VoyageState state, Player p, int rel_change){
		if(!this.planche.containsKey(p)) throw new IllegalArgumentException("Color is not present in the current game.");
		int position = this.planche.get(p);
		int count = rel_change > 0 ? rel_change : -rel_change;
		while(count>0){
			position += rel_change>=0 ? 1 : -1;
			if(this.getPlayerAt(position)!=null) continue;
			count--;
		}
		if(rel_change>0){
			for(Player other : this.planche.keySet()){
				if(p.equals(other)||p.getRetired()) continue;				
				if(this.planche.get(p)-this.planche.get(other)>=this.length){
					state.loseGame(other);
				}
			}
		}
		else{
			for(Player other : this.planche.keySet()){
				if(p.equals(other)||p.getRetired()) continue;
				if(this.planche.get(other)-this.planche.get(p)>=this.length){
					state.loseGame(p);
					return;
				}
			}
		}
		this.planche.put(p, position);
	}

	@Override
	public void loseGame(Player p) {
		this.planche.remove(p);
	}

	@Override
    public void printOrder() {
        List<Player> tmp = this.planche.keySet().stream().sorted((p1, p2) -> this.planche.get(p1) < this.planche.get(p2) ? 1 : -1).toList();
		for(Player p : tmp){
			System.out.println(p.getUsername()+" : "+this.getPlayerPosition(p));
		}
    }

}

