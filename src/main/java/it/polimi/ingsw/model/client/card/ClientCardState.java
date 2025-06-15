package it.polimi.ingsw.model.client.card;

import java.io.Serializable;

/**
 * Interface representing a client side {@link it.polimi.ingsw.model.cards.state.CardState}, that can display its info.
 */
public interface ClientCardState extends Serializable {

	/**
	 * Display the card state to a {@link ClientCardStateVisitor}.
	 * 
	 * @param visitor {@link ClientCardStateVisitor} Visitor displaying the card.
	 */
	void showCardState(ClientCardStateVisitor visitor);
}
