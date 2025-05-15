//Done.
package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.state.CardState;
import it.polimi.ingsw.model.state.VoyageState;

import java.io.Serializable;

public interface iCard extends Serializable {
	int getId();

	int getDays();

	CardState getState(VoyageState state);

	boolean getExhausted();
}