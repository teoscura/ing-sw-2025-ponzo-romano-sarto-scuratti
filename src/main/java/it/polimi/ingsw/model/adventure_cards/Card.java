package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.model.player.iSpaceShip;

public abstract class Card implements iCard {
    
    private int id;
    
    

    protected Card(int id){
        this.id = id;
    }

    @Override
    public int getId(){
        return this.id;
    }

    @Override
    public abstract void apply(iSpaceShip state, iPlayerResponse response);

    //TODO: add general methods
}
