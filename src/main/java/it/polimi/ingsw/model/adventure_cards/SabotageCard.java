//Done.
package it.polimi.ingsw.model.adventure_cards;

import java.util.concurrent.ThreadLocalRandom;

import it.polimi.ingsw.model.adventure_cards.utils.DaysCardResponse;
import it.polimi.ingsw.model.adventure_cards.utils.iCardResponse;
import it.polimi.ingsw.model.adventure_cards.utils.iPlayerResponse;
import it.polimi.ingsw.model.components.iBaseComponent;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.player.iSpaceShip;

public class SabotageCard extends Card{
      
    ShipCoords target = null;

    public SabotageCard(int id){
        super(id,0);
    }

    @Override
    public iCardResponse apply(iSpaceShip ship, iPlayerResponse response){
        int i = ThreadLocalRandom.current().nextInt(0, ship.getHeight());
        int j = ThreadLocalRandom.current().nextInt(0, ship.getWidth());
        this.target = new ShipCoords(ship.getType(), i, j);
        iBaseComponent tmp = ship.getComponent(target);
        while(tmp==ship.getEmpty()){
            i = ThreadLocalRandom.current().nextInt(0, ship.getHeight());
            j = ThreadLocalRandom.current().nextInt(0, ship.getWidth());
            this.target = new ShipCoords(ship.getType(), i, j);
            tmp = ship.getComponent(target);
        }
        ship.removeComponent(target);
        return new DaysCardResponse(0);
    }
    
}