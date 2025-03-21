package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.model.adventure_cards.utils.iCardResponse;
import it.polimi.ingsw.model.adventure_cards.utils.iPlayerResponse;
import it.polimi.ingsw.model.player.iSpaceShip;

public class SlaversCard extends Card{
    // int cannon_power_needed;
    // int crew_lost;
    // int coins_earned;

    public SlaversCard(int id, int days){
        super(id, days);
        //TODO
    }

    @Override
    public iCardResponse apply(iSpaceShip ship, iPlayerResponse response){
        //TODO
        return -days;
    }

    

}

