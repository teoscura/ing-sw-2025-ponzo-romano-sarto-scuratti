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

    public void setCrew(int new_crew, AlienType type){
        //TODO change type implem. im not checking if i can contain.
        if(new_crew<0){
            throw new NegativeArgumentException();
        }
        if(new_crew>max_capacity){
            throw new ArgumentTooBigException();
        }
        this.crew_number = new_crew;
    }

    private void upgradeCrewType(AlienType type){
        if(this.can_contain == AlienType.BOTH) return;
        if(type==AlienType.BOTH || type==AlienType.HUMAN) throw new IllegalArgumentException();
        if(this.can_contain == AlienType.HUMAN){
            this.can_contain = type;
            return;
        }
        if(this.can_contain!=type){
            this.can_contain = AlienType.BOTH;
        }
    }
    
    public void updateCrewType(iSpaceShip state, int position){
        CabinVisitor v = new CabinVisitor();
        iBaseComponent up = state.getComponent(state.up(position));
        iBaseComponent right = state.getComponent(state.down(position));
        iBaseComponent down = state.getComponent(state.left(position));
        iBaseComponent left = state.getComponent(state.right(position));
        if(up.getConnector(ComponentRotation.PI).connected(this.getConnector(ComponentRotation.ZERO))){
            up.check(v);
            this.upgradeCrewType(v.getType());
            v.reset();
        }
        if(right.getConnector(ComponentRotation.MINHALFPI).connected(this.getConnector(ComponentRotation.POSHALFPI))){
            right.check(v);
            this.upgradeCrewType(v.getType());
            v.reset(); 
        }
        if(down.getConnector(ComponentRotation.ZERO).connected(this.getConnector(ComponentRotation.PI))){
            down.check(v);
            this.upgradeCrewType(v.getType());
            v.reset();
        }
        if(left.getConnector(ComponentRotation.POSHALFPI).connected(this.getConnector(ComponentRotation.MINHALFPI))){
            left.check(v);
            this.upgradeCrewType(v.getType());
            v.reset();
        }
    }
}

