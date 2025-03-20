package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.model.adventure_cards.events.iCEvent;
import it.polimi.ingsw.model.player.iSpaceShip;

public class SmugglersCard extends Card{
    // int cannon_power_required;
    // int red_material;
    // int blue_material;
    // int green_material;
    // int yellow_material;
    // int days_spent;
    // int good_lost; //(take your n most valuable goods. If you run out of goods, they take batteries instead.) 

    public SmugglersCard(int id, int days){
        super(id, days);
        //TODO
    }

    @Override
    public iCEvent setup(iSpaceShip state) {
        // TODO Auto-generated method stub.
        throw new UnsupportedOperationException("Unimplemented method 'setup'");
    }

    @Override
    public int apply(iSpaceShip state, iPlayerResponse response){
        //TODO
        return 0;
    }

}
