package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.cards.LevelOneCardFactory;
import it.polimi.ingsw.model.cards.iCard;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A pile of {@link iCard} containing only eight specific level one cards, and no cards available to see during the construction phase.
 */
public class TestFlightCards implements iCards {

	private final ArrayDeque<iCard> cards;

	public TestFlightCards() {
		LevelOneCardFactory l1 = new LevelOneCardFactory();
		ArrayList<iCard> test_cards = new ArrayList<>() {{
			add(l1.getCard(2));
			add(l1.getCard(4));
			add(l1.getCard(5));
			add(l1.getCard(9));
			add(l1.getCard(13));
			add(l1.getCard(16));
			add(l1.getCard(18));
			add(l1.getCard(19));
		}};
		Collections.shuffle(test_cards);
		this.cards = new ArrayDeque<iCard>(test_cards);
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public iCard pullCard() {
		return this.cards.poll();
	}

	/**
	 * Returns always null as the Test Flight has no visible cards during construction.
	 * 
	 * @return null;
	 */
	@Override
	public List<Integer> getConstructionCards() {
		return null;
	}

	@Override
	public int getLeft() {
		return this.cards.size();
	}

}