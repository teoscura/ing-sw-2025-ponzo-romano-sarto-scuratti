//Done.
package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.model.adventure_cards.utils.*;
import it.polimi.ingsw.model.player.iSpaceShip;

public class CombatZoneCard extends Card{

    private final iCardResponse[] responses;

    public CombatZoneCard(int id, int days, int crew, iCardResponse[] responses){
        super(id, 0);
        if(responses==null) throw new NullPointerException();
        if(responses.length!=3) throw new IllegalArgumentException("Lines isn't lenght three.");
        for(iCardResponse r : responses){
            if(r==null) throw new NullPointerException();
        }
        this.responses = responses;
    }

    @Override
    public boolean hasMultipleRequirements(){
        return true;
    }

    //0: Least Power 1: Least Engine 2: Least Crew
    @Override
    public iCardResponse apply(iSpaceShip ship, iPlayerResponse response){
        if(ship==null || response == null) throw new NullPointerException();
        switch(response.getId()){
            case 0: return this.responses[0];
            case 1: return this.responses[1];
            case 2: return this.responses[2];
            default: throw new IllegalArgumentException("Invalid criteria id.");
        }
    }
}
