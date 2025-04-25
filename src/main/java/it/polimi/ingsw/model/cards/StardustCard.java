//Done.
package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.state.CardState;
import it.polimi.ingsw.model.cards.state.StardustState;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.state.VoyageState;

public class StardustCard extends Card {

    public StardustCard(int id){
        super(id, 0);
    }

	@Override
	public CardState getState(VoyageState state) {
		return new StardustState(state, this);
	}

	public void apply(VoyageState state, Player p) {
        if(state==null||p==null) throw new NullPointerException();
		int lost_days = p.getSpaceShip().countExposedConnectors();
        if(lost_days!=0) state.getPlanche().movePlayer(state, p, -lost_days);
	}

}