package it.polimi.ingsw.model.components;

enum ConnectorType{
    EMPTY,
    SINGLE_CONNECTOR,
    DOUBLE_CONNECTOR,
    UNIVERSAL,
}

//Defined clockwise.
enum ComponentRotation {
    ZERO, 
    POSHALFPI,
    PI,
    MINHALFPI,
}

public interface iBaseComponent {
    
    public ConnectorType[] getConnectors();

    public ComponentRotation getRotation();

    public boolean verify(iSpaceShip state, int position);

    public void check(iVisitor v);
    
}
