package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.exceptions.ArgumentTooBigException;
import it.polimi.ingsw.model.adventure_cards.exceptions.CrewSizeException;
import it.polimi.ingsw.model.player.iSpaceShip;

public class AbandonedStationCard extends Card{
    
    private int[] contains;
    private int contains_num;
    private int crew;
    private int days;


    public AbandonedStationCard(int id, int crew, int days, int[] contains){
        super(id);
        int s = 0;
        if(contains.length!=4) throw new IllegalArgumentException("Contains array isn't lenght 4.");
        for(int t : contains) if(t<0) throw new IllegalArgumentException("Contains cell is negative.");
        this.contains = contains;
        this.crew = crew;
        this.days = days;
    }

    @Override
    public int apply(iSpaceShip state, iPlayerResponse response){
        int sum = 0;
        for(int t: state.getCrew()) sum+= t;
        if(sum<this.crew) throw new CrewSizeException("Crew size too small to visit abandoned station.");
        if(response.getCoordArray().length>this.contains_num) throw new ArgumentTooBigException("Too many positions");
        if() //TODO;
        
        return 0;
    }

}