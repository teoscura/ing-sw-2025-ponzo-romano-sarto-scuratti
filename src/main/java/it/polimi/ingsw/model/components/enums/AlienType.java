//Done.
package it.polimi.ingsw.model.components.enums;

public enum AlienType{
    HUMAN (2, false, 0),
    BROWN (1, true, 1),
    PURPLE (1, true, 2),
    BOTH (1, false, -1);//Cabina collegata both support

    private int max_capacity;
    private boolean lifesupport_exists;
    private int arraypos;

    AlienType(int max_capacity, boolean lifesupport_exists, int arraypos){
        this.max_capacity = max_capacity;
        this.lifesupport_exists = lifesupport_exists;
        this.arraypos = arraypos;
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

}
