//Done.
package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.model.adventure_cards.events.vEmptyEvent;
import it.polimi.ingsw.model.adventure_cards.utils.iPlayerResponse;
import it.polimi.ingsw.model.adventure_cards.events.iCEvent;
import it.polimi.ingsw.model.player.iSpaceShip;

public class OpenSpaceCard extends Card {

    protected OpenSpaceCard(int id) {
        super(id, 0);
    }

    public int apply(iSpaceShip state, iPlayerResponse response){
       return state.getEnginePower();
    }

}
