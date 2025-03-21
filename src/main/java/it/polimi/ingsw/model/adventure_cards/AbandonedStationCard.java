package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.exceptions.ArgumentTooBigException;
import it.polimi.ingsw.exceptions.NegativeArgumentException;
import it.polimi.ingsw.model.adventure_cards.exceptions.CrewSizeException;
import it.polimi.ingsw.model.adventure_cards.utils.DaysCardResponse;
import it.polimi.ingsw.model.adventure_cards.utils.Planet;
import it.polimi.ingsw.model.adventure_cards.utils.PlanetCardResponse;
import it.polimi.ingsw.model.adventure_cards.utils.iCardResponse;
import it.polimi.ingsw.model.adventure_cards.utils.iPlayerResponse;
import it.polimi.ingsw.model.player.iSpaceShip;

public class AbandonedStationCard extends Card{
    
    private Planet planet;
    private int crew;

    public AbandonedStationCard(int id, Planet planet, int crew){
        super(id, 0);
        if(crew<=0) throw new NegativeArgumentException("Crew required can't be negative.");
        if(planet==null) throw new NullPointerException();
        this.crew=crew;
        this.planet=planet;
    }

    @Override
    public iCardResponse apply(iSpaceShip ship, iPlayerResponse response){
        if(response.getCoordArray().length>this.planet.getTotalQuantity()) throw new ArgumentTooBigException("Too many positions");
        if(!response.getAccept()) return new DaysCardResponse(0);
        if(ship.getTotalCrew()<this.crew) throw new CrewSizeException("Crew too small to salvage station.");
        this.exhaust();
        return new PlanetCardResponse(this.planet);
    }
}