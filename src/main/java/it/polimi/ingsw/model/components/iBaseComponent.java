//Done.
package it.polimi.ingsw.model.components;

import it.polimi.ingsw.model.components.visitors.iVisitor;
import it.polimi.ingsw.model.player.iSpaceShip;



public interface iBaseComponent {
    
    //Connectors are stored in this order: UP, RIGHT, DOWN, LEFT;
    public ConnectorType[] getConnectors();
    public ComponentRotation getRotation();
    public boolean verify(iSpaceShip state);
    public void check(iVisitor v);

    //Return the connector pointing up.
    public ConnectorType getConnector(ComponentRotation direction);
}

enum ConnectorType{
    EMPTY (0),
    SINGLE_CONNECTOR (1),
    DOUBLE_CONNECTOR (2),
    UNIVERSAL (-1);

    private int value;

    ConnectorType(int value){
        this.value = value;
    }
    
    private int getValue(){
        return this.value;
    }

    public boolean compatible(ConnectorType other){
        if(other.getValue()*this.getValue()<=0) return true;
        else return this.getValue() == other.getValue();
    }

    public boolean connected(ConnectorType other){
        if(other.getValue()==0 || this.getValue()==0) return false;
        return this.compatible(other);
    }
}

//Defined clockwise; Up is zero.
enum ComponentRotation {
    ZERO (0), 
    POSHALFPI (1),
    PI (2), 
    MINHALFPI (3);

    private int shift;

    ComponentRotation(int shift){
        this.shift = shift;
    }

    public int getShift(){
        return this.shift;
    }
}