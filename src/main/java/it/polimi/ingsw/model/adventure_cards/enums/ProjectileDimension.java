package it.polimi.ingsw.model.adventure_cards.enums;

public enum ProjectileDimension{
    
    BIG(false),
    SMALL(true);

    private boolean blockable;

    ProjectileDimension(boolean blockable){
        this.blockable = blockable;
    }

    public boolean getBlockable(){
        return this.blockable;
    }
}
