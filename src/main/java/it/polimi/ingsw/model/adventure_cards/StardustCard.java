//Done.
package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.adventure_cards.state.CardState;
import it.polimi.ingsw.model.adventure_cards.state.StardustState;
import it.polimi.ingsw.model.player.Player;

public class StardustCard extends Card {

    public StardustCard(int id){
        super(id, 0);
    }

	@Override
	public CardState getState(ModelInstance model) {
		return new StardustState(model, this);
	}

	public void apply(ModelInstance model, Player p) {
        if(model==null||p==null) throw new NullPointerException();
		int lost_days = p.getSpaceShip().countExposedConnectors();
        if(lost_days!=0) model.getPlanche().movePlayer(p.getColor(), -lost_days);
	}

}