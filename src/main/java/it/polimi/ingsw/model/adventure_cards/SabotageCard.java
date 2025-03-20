package it.polimi.ingsw.model.adventure_cards;

import java.util.concurrent.ThreadLocalRandom;

import it.polimi.ingsw.model.adventure_cards.events.iCEvent;
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
    public iCEvent setup(iSpaceShip ship) {
        //TODO;
        return null;
    }

    @Override
    public int apply(iSpaceShip ship, iPlayerResponse response){
        this.target = new ShipCoords(ship.getType(),
            ThreadLocalRandom.current().nextInt(0, ship.getHeight()),
            ThreadLocalRandom.current().nextInt(0, ship.getWidth()));
        iBaseComponent tmp = null;
        do{
            tmp = ship.getComponent(this.target);
        }while(ship.isEmpty(tmp));
        ship.removeComponent(target);
        return 0;
    }
    
}