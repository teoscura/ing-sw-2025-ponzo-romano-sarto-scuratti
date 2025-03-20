//Done.
package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.model.adventure_cards.utils.iPlayerResponse;
import it.polimi.ingsw.model.adventure_cards.visitors.CrewRemoveVisitor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.player.iSpaceShip;

public class AbandonedShipCard extends Card{

    private int credits_gained;
    private int crew_lost;

    public AbandonedShipCard(int id, int days, int crew_lost, int credits_gained){
        super(id, days);
        if(crew_lost <= 0 || credits_gained<= 0) throw new IllegalArgumentException("Negative arguments not allowed.");
        this.crew_lost = crew_lost;
        this.credits_gained = credits_gained;
    }

    @Override
    public int apply(iSpaceShip ship, iPlayerResponse response){
        if(response.getCoordArray().length != crew_lost) throw new IllegalArgumentException("The list of cells isn't large enough for this abandoned ship.");
        CrewRemoveVisitor v = new CrewRemoveVisitor();
        for(ShipCoords t : response.getCoordArray()){
            ship.getComponent(t).check(v);
        }
        ship.giveCredits(this.credits_gained);
        return -this.days;
    }

}