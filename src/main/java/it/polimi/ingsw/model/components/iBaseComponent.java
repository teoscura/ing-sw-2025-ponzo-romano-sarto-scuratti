package it.polimi.ingsw.model.components;

enum ConnectorType{
    EMPTY,
    SINGLE_CONNECTOR,
    DOUBLE_CONNECTOR,
    UNIVERSAL,
}

public interface iBaseComponent {
    
    public ConnectorType[] getConnectors();

    public 
}
