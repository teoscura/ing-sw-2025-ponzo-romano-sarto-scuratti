//Done.
package it.polimi.ingsw.model.adventure_cards.utils;

import it.polimi.ingsw.exceptions.NegativeArgumentException;
import it.polimi.ingsw.model.adventure_cards.exceptions.AlreadyVisitedException;

public class Planet {
    
    private int[] contains;
    private boolean visited = false;

    public Planet(int[] contains){
        if(contains.length!=4) throw new IllegalArgumentException("Array provided doesn't match number of possible shipments.");
        for(int t : contains){
            if(t<0) throw new NegativeArgumentException("Container quantity can't be less than zero.");
        }
        this.contains = contains;
    }

    public int[] getContains(){
        return this.contains;
    }

    public void visit(){
        if(this.visited) throw new AlreadyVisitedException();
        this.visited = true;
    }

    public boolean getVisited(){
        return this.visited;
    }
    
}
