//Done.
package it.polimi.ingsw.model.components.enums;

public enum AlienType{
    HUMAN (2, false, 0),
    BROWN (1, true, 1),
    PURPLE (1, true, 2),
    BOTH (1, false, -1);//Cabina collegata sia a support viola che marrone

    private int max_capacity;
    private boolean lifesupport_exists;
    private int arraypos;
    private int ncrewtypes = 3;

    AlienType(int max_capacity, boolean lifesupport_exists, int arraypos){
        this.max_capacity = max_capacity;
        this.arraypos = arraypos;
        this.lifesupport_exists = lifesupport_exists;
    }

    public int getMaxCapacity(){
        return this.max_capacity;
    }

    public boolean getLifeSupportExists(){
        return this.lifesupport_exists;
    }

    public int getArraypos(){
        return this.arraypos;
    }

    public int getNCrewTypes(){
        return this.ncrewtypes;
    }
}
