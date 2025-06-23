package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.cards.LevelOneCardFactory;
import it.polimi.ingsw.model.cards.LevelTwoCardFactory;
import it.polimi.ingsw.model.cards.iCard;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A pile of {@link iCard} containing both level one and two cards, and a set of cards available to see during the construction phase.
 */
public class LevelTwoCards implements iCards {

	private final ArrayDeque<iCard> cards;
	private final ArrayList<Integer> construction_cards;

	public LevelTwoCards() {
		LevelOneCardFactory l1 = new LevelOneCardFactory();
		LevelTwoCardFactory l2 = new LevelTwoCardFactory();
		ArrayList<iCard> lv1_cards = new ArrayList<>();
		for (int i = 1; i <= 20; i++) {
			lv1_cards.add(l1.getCard(i));
		}
		ArrayList<iCard> lv2_cards = new ArrayList<>();
		for (int i = 101; i <= 120; i++) {
			lv2_cards.add(l2.getCard(i));
		}
		Collections.shuffle(lv1_cards);
		Collections.shuffle(lv2_cards);
		ArrayList<iCard> tmp = new ArrayList<>();
		for (int i = 0; i < 4; i++) {
			tmp.add(lv2_cards.removeFirst());
			tmp.add(lv2_cards.removeFirst());
			tmp.add(lv1_cards.removeFirst());
		}
		while (tmp.getFirst().getId() < 100) {
			iCard shuffled = tmp.removeFirst();
			tmp.addLast(shuffled);
		}
		this.cards = new ArrayDeque<iCard>(tmp);
		this.construction_cards = new ArrayList<>(tmp.stream().map((c) -> c.getId()).toList().subList(0, 9));
		Collections.shuffle(this.construction_cards);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public iCard pullCard() {
		if (this.cards == null) return null;
		return this.cards.poll();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Integer> getConstructionCards() {
		return this.construction_cards;
	}

	@Override
	public int getLeft() {
		return this.cards.size();
	}

}
