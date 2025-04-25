//Done.
package it.polimi.ingsw.model.cards;

import java.io.Serializable;

import it.polimi.ingsw.model.cards.state.CardState;
import it.polimi.ingsw.model.state.VoyageState;

public interface iCard extends Serializable {
	int getId();

	int getDays();

	CardState getState(VoyageState state);

	boolean getExhausted();
}