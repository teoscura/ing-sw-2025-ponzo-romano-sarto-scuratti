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

    public ComponentRotation getOpposite(){
        switch(this.shift){
            case 0: return ComponentRotation.U180;
            case 1: return ComponentRotation.U270;
            case 2: return ComponentRotation.U000;
            case 3: return ComponentRotation.U090;
        }
        return ComponentRotation.U000;
    }

}