package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.model.adventure_cards.utils.BrokenCenterCabinResponse;
import it.polimi.ingsw.model.adventure_cards.utils.DaysCardResponse;
import it.polimi.ingsw.model.adventure_cards.utils.Projectile;
import it.polimi.ingsw.model.adventure_cards.utils.ProjectileArray;
import it.polimi.ingsw.model.adventure_cards.utils.iCardResponse;
import it.polimi.ingsw.model.adventure_cards.utils.iPlayerResponse;
import it.polimi.ingsw.model.player.iSpaceShip;

public class MeteorSwarmCard extends Card{

    private ProjectileArray meteorites;

    public MeteorSwarmCard(int id, ProjectileArray meteorites){
        super(id, 0);
        this.meteorites = meteorites;
    }
    //XXX risCRIVERE BENE
    @Overrideasdasdasd
    public iCardResponse apply(iSpaceShip ship, iPlayerResponse response){
        boolean broken_center_cabin = false;
        if(ship==null) throw new NullPointerException();
        for(Projectile p : this.meteorites.getProjectiles()){
            broken_center_cabin = ship.handleMeteorite(p);
        }
        if(broken_center_cabin) return new BrokenCenterCabinResponse();
        return new DaysCardResponse(0);
    }

}

