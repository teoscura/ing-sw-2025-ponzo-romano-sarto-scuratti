//Done.
package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.model.adventure_cards.utils.DaysCardResponse;
import it.polimi.ingsw.model.adventure_cards.utils.iCardResponse;
import it.polimi.ingsw.model.adventure_cards.utils.iPlayerResponse;
import it.polimi.ingsw.model.player.iSpaceShip;

public class StardustCard extends Card {

    public StardustCard(int id){
        super(id, 0);
    }

    @Override
    public iCardResponse apply(iSpaceShip ship, iPlayerResponse response){
        int lost_days = ship.countExposedConnectors();
        return new DaysCardResponse(-lost_days);
    }
    
}