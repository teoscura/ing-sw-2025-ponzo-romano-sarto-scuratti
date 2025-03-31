package it.polimi.ingsw.model.components.enums;

public enum StorageType{
    DOUBLENORMAL (false, 2),
    TRIPLENORMAL (false, 3),
    SINGLESPECIAL (true, 1),
    DOUBLESPECIAL (true, 2);

    private boolean special;
    private int capacity;

    StorageType(boolean special, int capacity){
        this.special = special;
        this.capacity = capacity;
    }

    public boolean getSpecial(){
        return this.special;
    }

    public int getCapacity(){
        return this.capacity;
    }
}