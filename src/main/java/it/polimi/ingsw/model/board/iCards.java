package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.cards.iCard;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a pile of {@link iCard} that can be pulled.
 */
public interface iCards extends Serializable {

	/**
	 * Pull a {@link iCard} from the top of the pile.
	 * 
	 * @return {@link iCard} The card at the top, or null if the pile is exhausted.
	 */
	iCard pullCard();

	/**
	 * Get a list of IDs of the cards displayed during the construction phase.
	 * 
	 * @return An array of IDs of every card visible during the construction phase.
	 */
	List<Integer> getConstructionCards();

	int getLeft();
}

