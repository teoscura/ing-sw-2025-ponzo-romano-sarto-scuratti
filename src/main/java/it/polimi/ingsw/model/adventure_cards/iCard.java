package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.model.adventure_cards.utils.iCardResponse;
import it.polimi.ingsw.model.adventure_cards.utils.iPlayerResponse;
import it.polimi.ingsw.model.player.iSpaceShip;

//
public interface iCard {
    public int getId();
    public iCardResponse apply(iSpaceShip ship, iPlayerResponse response);
    public boolean getExhausted();
}