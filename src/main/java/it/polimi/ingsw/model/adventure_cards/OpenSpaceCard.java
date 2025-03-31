//Done.
package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.model.adventure_cards.responses.DaysCardResponse;
import it.polimi.ingsw.model.adventure_cards.responses.GameLostResponse;
import it.polimi.ingsw.model.adventure_cards.responses.iCardResponse;
import it.polimi.ingsw.model.adventure_cards.responses.iPlayerResponse;
import it.polimi.ingsw.model.player.iSpaceShip;

public class OpenSpaceCard extends Card {
    
    protected OpenSpaceCard(int id) {
        super(id, 0);
    }

    public iCardResponse apply(iSpaceShip ship, iPlayerResponse response){
        if(ship==null) throw new NullPointerException();
        return ship.getEnginePower()>0 ? new DaysCardResponse(ship.getEnginePower()) : new GameLostResponse(); //
    }

}
