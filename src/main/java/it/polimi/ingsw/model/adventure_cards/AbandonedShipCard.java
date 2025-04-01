//Done.
package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.adventure_cards.state.AbandonedShipAnnounceState;
import it.polimi.ingsw.model.adventure_cards.state.CardState;
import it.polimi.ingsw.model.adventure_cards.utils.CardOrder;
import it.polimi.ingsw.model.adventure_cards.utils.PlayerResponse;
import it.polimi.ingsw.model.player.Player;
    
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
    public CardState getState(ModelInstance model) {
        return new AbandonedShipAnnounceState(model, this, model.getOrder(CardOrder.NORMAL));
    }

    public int getCredits(){
        return this.credits_gained;
    }

    public int getCrewLost(){
        return this.getCrewLost();
    }

    public void apply(ModelInstance model, Player p, PlayerResponse response){ //TODO landingresponse.
        if(model==null||p==null||response==null) throw new NullPointerException();
        if(response.getId()==0){
            if(p.getSpaceShip().getTotalCrew()<=this.crew_lost) throw new IllegalArgumentException("The crew isn't big enough for this abandoned ship.");
            this.exhaust();
        }        
    }

}