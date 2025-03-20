package it.polimi.ingsw.model.board;

import it.polimi.ingsw.controller.match.MatchSettings;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.iSpaceShip;
import it.polimi.ingsw.model.rulesets.GameModeType;

import java.util.*;
import java.util.Map.Entry;

public class Planche implements iPlanche {

	private final HashMap<PlayerColor, Integer> planche;
	private final int length;

	public Planche(GameModeType type, MatchSettings settings){
		this.length = type.getLength();
		this.planche = new HashMap<PlayerColor, Integer>();
		int i = 0;
		for(PlayerColor p : PlayerColor.values()){
			if(i==settings.getPlayerNumber().getNumber()) break;
			planche.put(p, 0);
			i++;
		}
	}

	@Override
	public int getPlayerPosition(iSpaceShip ship) {
		return this.planche.get(ship.getColor());
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
	public void movePlayer(iSpaceShip ship, int rel_change) {
		if(!this.planche.containsKey(ship.getColor())) throw new IllegalArgumentException("Color is not present in the current game.");
		int position = (this.planche.get(ship.getColor())+rel_change) % this.length;
		if()
		//TODO.
	}



	@Override
	public List<PlayerColor> getOrder(){
		return this.planche.entrySet().stream().sorted().map(m -> m.getKey()).toList();
	}
}
