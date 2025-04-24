//Done.
package it.polimi.ingsw.model.adventure_cards;

import java.io.Serializable;

import it.polimi.ingsw.model.adventure_cards.state.CardState;
import it.polimi.ingsw.model.state.VoyageState;

public interface iCard extends Serializable {
	int getId();

	int getDays();

	CardState getState(VoyageState state);

	boolean getExhausted();
}