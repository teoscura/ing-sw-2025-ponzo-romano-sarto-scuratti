package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.exceptions.CardAlreadyExhaustedException;

/**
 * Abstract class representation of an adventure card.
 */
public abstract class Card implements iCard {

	private final int id;
	protected int days;
	private boolean exhausted;

	/**
	 * Constructs a {@link Card} object.
	 * 
	 * @param id Card ID.
	 * @param days Days the card takes.
	 */
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
