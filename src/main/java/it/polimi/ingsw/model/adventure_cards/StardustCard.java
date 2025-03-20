package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.model.adventure_cards.utils.iPlayerResponse;
import it.polimi.ingsw.model.player.iSpaceShip;

public class StardustCard extends Card {

    public StardustCard(int id){
        super(id, 0);
    }

    @Override
    public int apply(iSpaceShip ship, iPlayerResponse response){
        //TODO visitor count exposed positions.
        return 0;
    }
    
}