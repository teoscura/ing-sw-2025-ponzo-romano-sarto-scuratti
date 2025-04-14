package it.polimi.ingsw.model.board;

import java.util.List;

import it.polimi.ingsw.model.adventure_cards.iCard;

public interface iCards {
	public iCard pullCard();
	public List<Integer> getConstructionCards();
}
