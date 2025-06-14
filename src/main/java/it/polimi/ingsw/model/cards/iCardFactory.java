package it.polimi.ingsw.model.cards;

/**
 * Interface representing a card factory of any level.
 */
public interface iCardFactory {
	/**
	 * Construct a card from the specified ID.
	 * 
	 * @param id ID of the card being requested.
	 * @return {@link iCard} The card corresponding to the ID, or {@code null} if the ID is not present.
	 */
	iCard getCard(int id);
}
