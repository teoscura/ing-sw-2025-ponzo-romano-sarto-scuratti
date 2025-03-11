//Done.
package it.polimi.ingsw.model.components;

public enum AlienType{
    HUMAN (2, false),
    BROWN (1, true),
    PURPLE (1, true),
    BOTH (1, false);//Cabina collegata sia a support viola che marrone

    private int max_capacity;
    private boolean create_lifesupport;

    AlienType(int max_capacity, boolean need_lifesupport){
        this.max_capacity = max_capacity;
        this.create_lifesupport = need_lifesupport;
    }

    public int getMaxCapacity(){
        return this.max_capacity;
    }

    public boolean getNeedLifeSupport(){
        return this.need_lifesupport;
    }
}
