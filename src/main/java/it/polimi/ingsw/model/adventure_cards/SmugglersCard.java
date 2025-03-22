//Done.
package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.exceptions.NegativeArgumentException;
import it.polimi.ingsw.model.adventure_cards.utils.*;
import it.polimi.ingsw.model.player.iSpaceShip;

public class SmugglersCard extends Card{
    
    private final Planet reward;
    private final int cargo_taken;
    private final int min_power;
    

    public SmugglersCard(int id, Planet reward, int cargo_taken, int min_power){
        super(id, 0);
        if(reward == null) throw new NullPointerException();
        if(min_power<=0 || cargo_taken<=0) throw new NegativeArgumentException();
        this.reward = reward;
        this.cargo_taken = cargo_taken;
        this.min_power = min_power;
    }

    @Override
    public iCardResponse apply(iSpaceShip ship, iPlayerResponse response){
        if(ship==null) throw new NullPointerException();
        if(ship.getCannonPower()>this.min_power){
            this.exhaust();
            return new SmugglerCardRewardResponse(reward);
        }
        else if(ship.getCannonPower()==this.min_power){
            return new DaysCardResponse(0);
        }
        return new SmugglerCardPenaltyResponse(this.cargo_taken);
    }

}
