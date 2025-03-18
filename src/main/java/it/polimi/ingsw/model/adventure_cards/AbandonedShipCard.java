package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.model.player.iSpaceShip;

public class AbandonedShipCard extends Card{
    // private int crew_lost; //int (?) 
    // private int days_spent;
    // private int credits_gained;

    public AbandonedShipCard(int id){
        super(id);
        //TODO
    }

    @Override
    public int apply(iSpaceShip state, iPlayerResponse response){
        //TODO
        return 0;
    }

}