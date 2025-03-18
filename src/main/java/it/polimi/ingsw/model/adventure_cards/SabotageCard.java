package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.model.player.iSpaceShip;

public class SabotageCard extends Card{
    int miss = 0;

    public SabotageCard(int id){
        super(id);
    }

    @Override
    public int apply(iSpaceShip state, iPlayerResponse response){
        //TODO
        return 0;
    }

    //find player with lowest crew amount, if parity pick furthest one
    /*find_component(player){
        while (miss<3){
            throw 4 dice 2 for row and 2 for column
            if (no component hit){
                miss++;
            }
            else{
                remove component();
                return;
            }
        }
        return;
    }*/
}