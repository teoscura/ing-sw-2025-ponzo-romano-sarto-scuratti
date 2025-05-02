package it.polimi.ingsw.model.board;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.model.cards.LevelOneCardFactory;
import it.polimi.ingsw.model.cards.iCard;

public class DummyTestFlightCards implements iCards {

	private final ArrayDeque<iCard> cards;

	public DummyTestFlightCards() {
		LevelOneCardFactory l1 = new LevelOneCardFactory();
		ArrayList<iCard> test_cards = new ArrayList<>() {{
			add(l1.getCard(5));
			add(l1.getCard(4));
			add(l1.getCard(2));
			add(l1.getCard(9));
			add(l1.getCard(13));
			add(l1.getCard(16));
			add(l1.getCard(18));
			add(l1.getCard(19));
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