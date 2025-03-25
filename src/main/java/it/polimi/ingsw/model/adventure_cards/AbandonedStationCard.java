//Done.
package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.exceptions.ArgumentTooBigException;
import it.polimi.ingsw.exceptions.NegativeArgumentException;
import it.polimi.ingsw.model.adventure_cards.exceptions.CrewSizeException;
import it.polimi.ingsw.model.adventure_cards.responses.DaysCardResponse;
import it.polimi.ingsw.model.adventure_cards.responses.PlanetCardResponse;
import it.polimi.ingsw.model.adventure_cards.responses.iCardResponse;
import it.polimi.ingsw.model.adventure_cards.responses.iPlayerResponse;
import it.polimi.ingsw.model.adventure_cards.utils.Planet;
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
    public iCardResponse apply(iSpaceShip ship, iPlayerResponse response){
        if(ship==null || response == null) throw new NullPointerException();
        if(response.getCoordArray().length>this.planet.getTotalQuantity()) throw new ArgumentTooBigException("Too many positions");
        if(!response.getAccept()) return new DaysCardResponse(0);
        if(ship.getTotalCrew()<this.crew) throw new CrewSizeException("Crew too small to salvage station.");
        this.exhaust();
        return new PlanetCardResponse(this.planet, this.days);
    }
}