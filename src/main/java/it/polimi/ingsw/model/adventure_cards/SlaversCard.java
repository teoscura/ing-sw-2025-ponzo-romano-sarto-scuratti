//Done.
package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.exceptions.NegativeArgumentException;
import it.polimi.ingsw.model.adventure_cards.utils.*;
import it.polimi.ingsw.model.player.iSpaceShip;

public class SlaversCard extends Card{
    

    private final int min_power;
    private final int crew_penalty;
    private final int credits;

    public SlaversCard(int id, int days, int min_power, int crew_penalty, int credits){
        super(id, days);
        if(credits<=0||crew_penalty<=0) throw new NegativeArgumentException();
        this.min_power = min_power;
        this.crew_penalty = crew_penalty;
        this.credits = credits;
    }

    @Override
    public iCardResponse apply(iSpaceShip ship, iPlayerResponse response){
        if(ship.getCannonPower()>this.min_power){
            this.exhaust();
            return new PirateCardReward(credits, days);
        }
        else if(ship.getCannonPower()==this.min_power){
            return new DaysCardResponse(0);
        }
        return new StaffCardResponse(-this.crew_penalty);
    }

    

}

