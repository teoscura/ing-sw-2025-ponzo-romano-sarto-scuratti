package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.adventure_cards.iCard;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Queue;

public class Cards implements iCards {

	private Queue<iCard> cards;

	public Cards(Collection<iCard> cards){
		this.cards = new ArrayDeque<iCard>(cards);
	}

	@Override
	public iCard pullCard() {
		if(this.cards == null) return null;
		return this.cards.poll();
	}
}
