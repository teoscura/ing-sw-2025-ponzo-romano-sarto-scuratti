package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.model.player.iSpaceShip;

public interface iCard {
    public int getId();
    public int apply(iSpaceShip player, iPlayerResponse response);
    public boolean getExhausted();
}


