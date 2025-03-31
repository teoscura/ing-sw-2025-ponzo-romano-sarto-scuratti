//Done.
package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.message.client.AskLandingMessage;
import it.polimi.ingsw.message.client.AskRemoveCrewMessage;
import it.polimi.ingsw.message.client.ClientMessage;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.adventure_cards.utils.CardOrder;
import it.polimi.ingsw.model.adventure_cards.utils.CardResponseType;
import it.polimi.ingsw.model.adventure_cards.utils.PlayerResponse;
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
    public ClientMessage getRequest() {
        return new AskLandingMessage(this.days);
    }

    @Override
    public CardResponseType getResponse() {
        return CardResponseType.LAND_CHOICE;
    }

    @Override
    public CardResponseType getAfterResponse() {
        return this.after_response;
    }

    @Override
    public CardOrder getOrder() {
        return CardOrder.NORMAL;
    }

    @Override
    public ClientMessage apply(ModelInstance model, iSpaceShip ship, PlayerResponse response){
        if(model==null||ship==null||response==null) throw new NullPointerException();
        this.after_response = CardResponseType.NONE;
        if(response.getId()==0){
            if(ship.getTotalCrew()<=this.crew_lost) throw new IllegalArgumentException("The crew isn't big enough for this abandoned ship.");
            ship.giveCredits(this.credits_gained);
            model.getPlanche().movePlayer(ship, this.days);
            this.exhaust();
            this.after_response = CardResponseType.REMOVE_CREW;
            return new AskRemoveCrewMessage(this.crew_lost);
        }        
        return null;
    }

}