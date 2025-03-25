package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.exceptions.OutOfBoundsException;
import it.polimi.ingsw.model.adventure_cards.responses.BrokenCenterCabinResponse;
import it.polimi.ingsw.model.adventure_cards.responses.DaysCardResponse;
import it.polimi.ingsw.model.adventure_cards.responses.iCardResponse;
import it.polimi.ingsw.model.adventure_cards.responses.iPlayerResponse;
import it.polimi.ingsw.model.adventure_cards.utils.ProjectileArray;
import it.polimi.ingsw.model.player.iSpaceShip;

public class MeteorSwarmCard extends Card{

    private final ProjectileArray meteorites;
    private int turn = 0;

    public MeteorSwarmCard(int id, ProjectileArray meteorites){
        super(id, 0);
        this.meteorites = meteorites;
    }

    @Override
    public boolean multiPhase(){
        return true;
    }

    @Override
    public void nextPhase(){
        if(this.turn==this.meteorites.getProjectiles().length-1) throw new OutOfBoundsException("Meteorite card is already at its last phase.");
        this.turn++;
    }

    @Override
    public iCardResponse apply(iSpaceShip ship, iPlayerResponse response){
        if(ship==null) throw new NullPointerException();
        boolean broken_center_cabin = ship.handleMeteorite(this.meteorites.getProjectiles()[turn]);
        if(broken_center_cabin) return new BrokenCenterCabinResponse();
        return new DaysCardResponse(0);
    }

}

