package it.polimi.ingsw.model.components.enums;

public enum EngineType{
    SINGLE (1, false),
    DOUBLE (2, true );

    private int max_power;
    private boolean powerable;

    EngineType(int max_power, boolean powerable){
        this.max_power = max_power;
        this.powerable = powerable;
    }

    public int getMaxPower(){
        return this.max_power;
    }

    public boolean getPowerable(){
        return this.powerable;
    }
}