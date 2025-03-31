//Done.
package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.model.adventure_cards.responses.BrokenCenterCabinResponse;
import it.polimi.ingsw.model.adventure_cards.responses.DaysCardResponse;
import it.polimi.ingsw.model.adventure_cards.responses.iCardResponse;
import it.polimi.ingsw.model.adventure_cards.responses.iPlayerResponse;
import it.polimi.ingsw.model.adventure_cards.utils.*;
import it.polimi.ingsw.model.player.iSpaceShip;

public class CombatZoneCard extends Card{

    private final ProjectileArray shots;
    private final iCardResponse[] responses;

    public CombatZoneCard(int id, iCardResponse[] responses, ProjectileArray shots){
        super(id, 0);
        if(responses==null || shots == null) throw new NullPointerException();
        if(responses.length!=3) throw new IllegalArgumentException("Lines isn't lenght three.");
        for(iCardResponse r : responses){
            if(r==null) throw new NullPointerException();
        }
        this.shots = shots;
        this.responses = responses;
    }
    
    //FIXME.
    @Override
    public boolean hasMultipleRequirements(){
        return true;
    }

    //0: Least Power 1: Least Engine 2: Least Crew 
    @Override
    public iCardResponse apply(iSpaceShip ship, iPlayerResponse response){
        if(ship==null || response == null) throw new NullPointerException();
        boolean broken_center_cabin = false;
        switch(response.getId()){
            case 0: {
                if(this.responses[0].hasShot()){
                    for(Projectile p : this.shots.getProjectiles()){
                        broken_center_cabin = ship.handleShot(p);
                    }
                    if(broken_center_cabin) return new BrokenCenterCabinResponse();
                    return new DaysCardResponse(0);
                }
                return this.responses[0];
            }
            case 1: {
                if(this.responses[1].hasShot()){
                    for(Projectile p : this.shots.getProjectiles()){
                        broken_center_cabin = ship.handleShot(p);
                    }
                    if(broken_center_cabin) return new BrokenCenterCabinResponse();
                    return new DaysCardResponse(0);
                }
                return this.responses[1];
            }
            case 2: {
                if(this.responses[2].hasShot()){
                    for(Projectile p : this.shots.getProjectiles()){
                        broken_center_cabin = ship.handleShot(p);
                    }
                    if(broken_center_cabin) return new BrokenCenterCabinResponse();
                    return new DaysCardResponse(0);
                }
                return this.responses[0];
            }
            default: throw new IllegalArgumentException("Invalid criteria id.");
        }
    }
}
