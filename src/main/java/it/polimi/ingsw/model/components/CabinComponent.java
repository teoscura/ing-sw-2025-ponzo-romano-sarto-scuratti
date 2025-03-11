package it.polimi.ingsw.model.components;

import it.polimi.ingsw.exceptions.ArgumentTooBigException;
import it.polimi.ingsw.exceptions.NegativeArgumentException;
import it.polimi.ingsw.model.components.visitors.CabinVisitor;
import it.polimi.ingsw.model.components.visitors.iVisitor;
import it.polimi.ingsw.model.player.iSpaceShip;

public class CabinComponent extends BaseComponent{
    private int max_capacity;
    private int crew_number = 0;
    private AlienType crew_type;
    private AlienType can_contain;

    public CabinComponent(ConnectorType[] connectors, 
                          ComponentRotation rotation,
                          AlienType inhabitant_type){
        super(connectors, rotation);
        this.max_capacity = inhabitant_type.getMaxCapacity(); 
    }

    public CabinComponent(ConnectorType[] connectors, 
                          ComponentRotation rotation,
                          AlienType inhabitant_type,
                          int position){
        super(connectors, rotation, position);
        this.max_capacity = inhabitant_type.getMaxCapacity(); 
    }


    @Override
    public void check(iVisitor v){
        v.visit(this);
    }

    public int getCrew(){
        return crew_number;
    }

    public AlienType getCrewType(){
        return this.crew_type;
    }

    public void setCrew(int new_crew){
        //TODO change type implem. im not checking if i can contain.
        if(new_crew<0){
            throw new NegativeArgumentException();
        }
        if(new_crew>max_capacity){
            throw new ArgumentTooBigException();
        }
        this.crew_number = new_crew;
    }
    
    public void updateCrewType(iSpaceShip state, int position){
        iVisitor v = new CabinVisitor();
        iBaseComponent up = state.getComponent(state.up(position));
        iBaseComponent right = state.getComponent(state.down(position));
        iBaseComponent down = state.getComponent(state.left(position));
        iBaseComponent left = state.getComponent(state.right(position));
        if(up.getConnector(ComponentRotation.PI).connected(this.getConnector(ComponentRotation.ZERO))){
            
        }
        if(right.getConnector(ComponentRotation.MINHALFPI).connected(this.getConnector(ComponentRotation.POSHALFPI))){
            
        }
        if(down.getConnector(ComponentRotation.ZERO).connected(this.getConnector(ComponentRotation.PI))){
            
        }
        if(left.getConnector(ComponentRotation.POSHALFPI).connected(this.getConnector(ComponentRotation.MINHALFPI))){
            
        }
        
        //TODO: tirare fuori dal visitor i tipi.
    }
}

