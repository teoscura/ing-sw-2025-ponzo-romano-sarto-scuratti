//Done.
package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.model.adventure_cards.state.CardState;
import it.polimi.ingsw.model.state.VoyageState;

public interface iCard {
    public int getId();
    public int getDays();
    public CardState getState(VoyageState state);
    public boolean getExhausted();
}