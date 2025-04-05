package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.adventure_cards.iCard;

public interface iCards {
	public iCard pullCard();
	public int[] getConstructionCards();
}
