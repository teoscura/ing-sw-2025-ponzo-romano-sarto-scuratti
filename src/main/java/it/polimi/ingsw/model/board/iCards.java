package it.polimi.ingsw.model.board;

import java.io.Serializable;
import java.util.List;

import it.polimi.ingsw.model.adventure_cards.iCard;

public interface iCards extends Serializable {
	iCard pullCard();

	int getLeft();

	List<Integer> getConstructionCards();
}
