//Done.
package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.exceptions.NegativeArgumentException;
import it.polimi.ingsw.model.adventure_cards.responses.BrokenCenterCabinResponse;
import it.polimi.ingsw.model.adventure_cards.responses.DaysCardResponse;
import it.polimi.ingsw.model.adventure_cards.responses.PirateCardReward;
import it.polimi.ingsw.model.adventure_cards.responses.iCardResponse;
import it.polimi.ingsw.model.adventure_cards.responses.iPlayerResponse;
import it.polimi.ingsw.model.adventure_cards.utils.Projectile;
import it.polimi.ingsw.model.adventure_cards.utils.ProjectileArray;
import it.polimi.ingsw.model.player.iSpaceShip;


public class PiratesCard extends Card{

    private final ProjectileArray shots;
    private final int credits;
    private final int min_power;

    public PiratesCard(int id, int days, ProjectileArray shots, int min_power, int credits){
        super(id, days);
        if(shots == null) throw new NullPointerException();
        if(min_power<=0 || credits <=0) throw new NegativeArgumentException("Pirate power/rewards cannot be less than one.");
        this.credits = credits;
        this.shots = shots;
        this.min_power = min_power;
    }

    @Override
    public iCardResponse apply(iSpaceShip ship, iPlayerResponse response){
        if(ship==null) throw new NullPointerException();
        boolean broken_center_cabin = false;
        if(ship.getCannonPower()>this.min_power){
            this.exhaust();
            return new PirateCardReward(this.credits, this.days);
        }
        else if(ship.getCannonPower()==this.min_power){
            return new DaysCardResponse(0);
        }
        for(Projectile p : this.shots.getProjectiles()){
            broken_center_cabin = ship.handleShot(p);
        }
        if(broken_center_cabin) return new BrokenCenterCabinResponse();
        return new DaysCardResponse(0);
    }
    
}
