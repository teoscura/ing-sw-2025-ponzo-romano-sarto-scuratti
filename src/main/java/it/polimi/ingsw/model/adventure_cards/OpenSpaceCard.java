//Done.
package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.model.player.iSpaceShip;

public class OpenSpaceCard {
   public int apply(iSpaceShip state, iPlayerResponse response){
       return state.getEnginePower();
    }
}
