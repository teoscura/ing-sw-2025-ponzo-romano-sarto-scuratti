//Done.
package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.model.adventure_cards.exceptions.AlreadyVisitedException;
import it.polimi.ingsw.model.components.enums.ShipmentType;

public class Planet {

    private int[] contains;
    private boolean visited = false;

    public Planet(int[] contains){
        if(contains.length!=4) throw new IllegalArgumentException("Array provided doesn't match number of possible shipments.");
        this.contains = contains;
    }

    public int getQuantity(ShipmentType type){
        if(type.getValue()==0) throw new IllegalArgumentException("Cannot ask for quantity of empty shipments.");
        return this.contains[type.getValue()-1];
    }

    public void visit(){
        if(this.visited) throw new AlreadyVisitedException();
        this.visited = true;
    }

    public boolean getVisited(){
        return this.visited;
    }
}
