package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.model.adventure_cards.events.iCEvent;
import it.polimi.ingsw.model.player.iSpaceShip;

public class StardustCard extends Card {
    /*for(every player reverse order ){
        //exposed_connectors = (TODO: function that counts exposed connectors)
        player.position -= exposed_connectors;
    }*/
    
    public StardustCard(int id){
        super(id, 0);
        //TODO
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