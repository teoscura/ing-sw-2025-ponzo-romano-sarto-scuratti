package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.model.adventure_cards.exceptions.CardAlreadyExhaustedException;

public abstract class Card implements iCard {

	private final int id;
	protected int days;
	private boolean exhausted;

	protected Card(int id, int days) {
		if (days < 0) throw new IllegalArgumentException("Negative arguments not allowed.");
		this.id = id;
		this.days = days;
	}

	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public int getDays() {
		return this.days;
	}

	@Override
	public boolean getExhausted() {
		return this.exhausted;
	}

	protected void exhaust() {
		if (this.exhausted) throw new CardAlreadyExhaustedException("This card's effect was already exhausted.");
		this.exhausted = true;
	}

}
