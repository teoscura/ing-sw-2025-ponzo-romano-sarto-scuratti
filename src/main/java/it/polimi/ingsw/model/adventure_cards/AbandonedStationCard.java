//Done.
package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.exceptions.NegativeArgumentException;
import it.polimi.ingsw.message.client.AskLandingMessage;
import it.polimi.ingsw.message.client.ClientMessage;
import it.polimi.ingsw.message.client.PlanetMessage;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.adventure_cards.exceptions.CrewSizeException;
import it.polimi.ingsw.model.adventure_cards.utils.CardOrder;
import it.polimi.ingsw.model.adventure_cards.utils.CardResponseType;
import it.polimi.ingsw.model.adventure_cards.utils.Planet;
import it.polimi.ingsw.model.adventure_cards.utils.PlayerResponse;
import it.polimi.ingsw.model.player.iSpaceShip;

public class AbandonedStationCard extends Card{
    
    private Planet planet;
    private int crew;

    public AbandonedStationCard(int id, int days, Planet planet, int crew){
        super(id, days);
        if(crew<=0) throw new NegativeArgumentException("Crew required can't be negative.");
        if(planet==null) throw new NullPointerException();
        this.crew=crew;
        this.planet=planet;
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
            if(ship.getTotalCrew()<this.crew) throw new CrewSizeException("Crew too small to salvage station.");
            this.exhaust();
            model.getPlanche().movePlayer(ship, -this.days);
            this.after_response = CardResponseType.TAKE_CARGO;
            return new PlanetMessage(this.planet.getContains(), this.days);
        }
        return null;
    }

}