//Done.
package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.model.adventure_cards.utils.StaffCardResponse;
import it.polimi.ingsw.model.adventure_cards.utils.iCardResponse;
import it.polimi.ingsw.model.adventure_cards.utils.iPlayerResponse;
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
    public iCardResponse apply(iSpaceShip ship, iPlayerResponse response){
        if(ship==null) throw new NullPointerException();
        if(ship.getTotalCrew()<=this.crew_lost) throw new IllegalArgumentException("The crew isn't big enough for this abandoned ship.");
        ship.giveCredits(this.credits_gained);
        return new StaffCardResponse(-this.crew_lost);
    }

}