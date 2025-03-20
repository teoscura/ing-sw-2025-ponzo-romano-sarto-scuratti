package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.exceptions.ArgumentTooBigException;
import it.polimi.ingsw.exceptions.NegativeArgumentException;
import it.polimi.ingsw.model.adventure_cards.events.iCEvent;
import it.polimi.ingsw.model.adventure_cards.events.vAbandonedStationInfoEvent;
import it.polimi.ingsw.model.adventure_cards.exceptions.CoordsIndexLenghtMismatchException;
import it.polimi.ingsw.model.adventure_cards.exceptions.CrewSizeException;
import it.polimi.ingsw.model.player.ShipCoords;
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
    public iCEvent setup(iSpaceShip ship) {
        return new vAbandonedStationInfoEvent(this.planet, this.crew);
    }

    @Override
    public int apply(iSpaceShip ship, iPlayerResponse response){
        if(response.getCoordArray().length>this.planet.getTotalQuantity()) throw new ArgumentTooBigException("Too many positions");
        validateCargoChoices(response.getCoordArray(), response.getMerchChoices());
        validateCrewNumber(ship);
        //FIXME finish.
   
        return -this.planet.getDays();
    }

    private void validateCargoChoices(ShipCoords[] coords, int[] cargo_indexes){
        //TODO. throw exceptions where needed.
        if(coords.length!=cargo_indexes.length) throw new CoordsIndexLenghtMismatchException("Storage coords and cargo locations aren't the same lenght.");
    }

    private void validateCrewNumber(iSpaceShip ship){
        int sum = 0;
        for(int t: ship.getCrew()) sum+= t;
        if(sum<this.crew) throw new CrewSizeException("Crew size too small to visit abandoned station.");
    }

    

}