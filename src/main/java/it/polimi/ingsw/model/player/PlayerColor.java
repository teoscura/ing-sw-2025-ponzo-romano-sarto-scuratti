package it.polimi.ingsw.model.player;

public enum PlayerColor {
    
    RED (0),
    BLUE (1),
    GREEN (2),
    YELLOW (3),
    NONE (-1);

    private int order;

    PlayerColor(int order){
        this.order = order;
    }

    public int getOrder(){
        return this.order;
    }
}

