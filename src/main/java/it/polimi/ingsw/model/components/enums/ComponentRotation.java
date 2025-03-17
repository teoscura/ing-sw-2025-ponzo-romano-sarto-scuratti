package it.polimi.ingsw.model.components.enums;

//Defined clockwise; Up is zero.
public enum ComponentRotation {
    U000 (0), 
    U090 (1),
    U180 (2), 
    U270 (3);

    private int shift;

    ComponentRotation(int shift){
        this.shift = shift;
    }

    public int getShift(){
        return this.shift;
    }
}