package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.model.adventure_cards.utils.iPlayerResponse;
import it.polimi.ingsw.model.player.iSpaceShip;

//
public interface iCard {
    public int getId();
    public boolean getDistanceOrder();
    public void needAsk();
    public int apply(iSpaceShip ship, iPlayerResponse response);
    public boolean getExhausted();
}


