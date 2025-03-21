package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.model.adventure_cards.utils.iPlayerResponse;
import it.polimi.ingsw.model.player.iSpaceShip;

//
public interface iCard {
    public int getId();
    public boolean getDistanceOrder();
    public void needAsk();
    public int apply(iSpaceShip ship, iPlayerResponse response);
    public int apply(iSpaceShip ship, iPlayerResponse response, int order);
    public boolean getExhausted();
}


if(getDistanceOrder()){
    if(needAsk()){
        for(int i=0;i<playerlist.size()&&!c.getExhausted();i++){ //AbandonedShip,Station,Planets
            iPlayerResponse res = player.awaitResponse(c);
            c.apply(playerlist[i], res);
        }
    }
    else for(iSpaceShip ship :  playerlist) c.apply(ship, null);
}