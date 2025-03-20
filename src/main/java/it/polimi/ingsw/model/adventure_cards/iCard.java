//Done.
package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.model.adventure_cards.events.iCEvent;
import it.polimi.ingsw.model.player.iSpaceShip;

public interface iCard {
    public int getId();
    public iCEvent setup(iSpaceShip ship);
    public int apply(iSpaceShip ship, iPlayerResponse response);
    public boolean getExhausted();
}


