package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.cards.iCard;

import java.io.Serializable;
import java.util.List;

public interface iCards extends Serializable {
	iCard pullCard();

	int getLeft();

	List<Integer> getConstructionCards();
}
