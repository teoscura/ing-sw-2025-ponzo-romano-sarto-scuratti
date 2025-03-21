package it.polimi.ingsw.model.components.enums;

public enum ConnectorType{
    EMPTY (0),
    SINGLE_CONNECTOR (1),
    DOUBLE_CONNECTOR (2),
    UNIVERSAL (-1);

    private int value;

    ConnectorType(int value){
        this.value = value;
    }
    
    public int getValue(){
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