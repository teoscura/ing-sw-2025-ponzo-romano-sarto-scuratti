package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.exceptions.ArgumentTooBigException;
import it.polimi.ingsw.model.adventure_cards.events.iCEvent;
import it.polimi.ingsw.model.adventure_cards.exceptions.CoordsIndexLenghtMismatchException;
import it.polimi.ingsw.model.adventure_cards.exceptions.CrewSizeException;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.player.iSpaceShip;

public class AbandonedStationCard extends Card{
    
    private int[] contains;
    private int contains_num;
    private int crew;
    private int days;


    public AbandonedStationCard(int id, int crew, int days, int[] contains){
        super(id);
        if(days<=0) throw new IllegalArgumentException("Negative arguments not allowed.");
        if(contains.length!=4) throw new IllegalArgumentException("Contains array isn't lenght 4.");
        for(int t : contains) if(t<0) throw new IllegalArgumentException("Contains cell is negative.");
        this.contains = contains;
        this.crew = crew;
        this.days = days;
    }

    @Override
    public iCEvent setup(iSpaceShip state) {
        //TODO show merch and crew cost.
        return null;
    }

    @Override
    public int apply(iSpaceShip ship, iPlayerResponse response){
        if(response.getCoordArray().length>this.contains_num) throw new ArgumentTooBigException("Too many positions");
        validateCargoChoices(response.getCoordArray(), response.getMerchChoices());
        validateCrewNumber(ship);
        //FIXME finish.
   
        return -this.days;
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