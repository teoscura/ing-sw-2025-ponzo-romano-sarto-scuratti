package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.model.adventure_cards.utils.iPlayerResponse;
import it.polimi.ingsw.model.player.iSpaceShip;

public class EpidemicCard extends Card{
    
    public EpidemicCard(int id){
        super(id,0);
    }

    @Override
    public int apply(iSpaceShip state, iPlayerResponse response){
        //TODO
        return 0;
    }

}
