package it.polimi.ingsw.model.board;

import java.io.Serializable;
import java.util.List;

import it.polimi.ingsw.model.adventure_cards.iCard;

public interface iCards extends Serializable {
	public iCard pullCard();
	public int getLeft();
	public List<Integer> getConstructionCards();
}
