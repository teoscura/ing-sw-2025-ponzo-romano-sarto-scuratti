//Done.
package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.adventure_cards.state.CardState;

public interface iCard {
    public int getId();
    public int getDays();
    public CardState getState(ModelInstance model);
    public boolean getExhausted();
}