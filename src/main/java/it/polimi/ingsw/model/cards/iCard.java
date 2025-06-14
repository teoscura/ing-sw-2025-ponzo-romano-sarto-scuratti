//Done.
package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.state.CardState;
import it.polimi.ingsw.model.state.VoyageState;

import java.io.Serializable;

/**
 * Interface representing the contract an adventure card must respect.
 */
public interface iCard extends Serializable {
	int getId();

	int getDays();

	/**
	 * Returns the starting point of the state automata that represents the {@link iCard}.
	 * 
	 * @param state {@link VoyageState} VoyageState that owns the card.
	 * @return {@link CardState} Starting point of the FSA.
	 */
	CardState getState(VoyageState state);

	boolean getExhausted();
}