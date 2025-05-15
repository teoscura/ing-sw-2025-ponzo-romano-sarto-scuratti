package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.cards.LevelOneCardFactory;
import it.polimi.ingsw.model.cards.iCard;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class OneFlightCards implements iCards {

	private final ArrayDeque<iCard> cards;

	public OneFlightCards() {
		LevelOneCardFactory l1 = new LevelOneCardFactory();
		ArrayList<iCard> test_cards = new ArrayList<>() {{
			add(l1.getCard(5));
		}};
		this.cards = new ArrayDeque<iCard>(test_cards);
	}

	@Override
	public iCard pullCard() {
		return this.cards.poll();
	}

	@Override
	public List<Integer> getConstructionCards() {
		return null;
	}

	@Override
	public int getLeft() {
		return this.cards.size();
	}

}
