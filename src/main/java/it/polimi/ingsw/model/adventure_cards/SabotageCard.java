package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.model.adventure_cards.events.iCEvent;
import it.polimi.ingsw.model.player.iSpaceShip;

public class SabotageCard extends Card{
    int miss = 0;

    public SabotageCard(int id){
        super(id);
    }

    @Override
    public iCEvent setup(iSpaceShip state) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setup'");
    }


    @Override
    public int apply(iSpaceShip state, iPlayerResponse response){
        //TODO
        return 0;
    }
    
}