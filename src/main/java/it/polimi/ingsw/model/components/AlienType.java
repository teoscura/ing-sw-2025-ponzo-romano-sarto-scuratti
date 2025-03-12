//Done.
package it.polimi.ingsw.model.components;

public enum AlienType{
    HUMAN (2, false),
    BROWN (1, true),
    PURPLE (1, true),
    BOTH (1, false);//Cabina collegata sia a support viola che marrone

    private int max_capacity;
    private boolean lifesupport_exists;

    AlienType(int max_capacity, boolean lifesupport_exists){
        this.max_capacity = max_capacity;
        this.lifesupport_exists = lifesupport_exists;
    }

    public int getMaxCapacity(){
        return this.max_capacity;
    }

    public boolean getLifeSupportExists(){
        return this.lifesupport_exists;
    }
}
