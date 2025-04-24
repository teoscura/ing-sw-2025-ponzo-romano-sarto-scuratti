package it.polimi.ingsw.model.adventure_cards.utils;

public enum ProjectileDimension {

	BIG(false),
	SMALL(true);

	private final boolean blockable;

	ProjectileDimension(boolean blockable) {
		this.blockable = blockable;
	}

	public boolean getBlockable() {
		return this.blockable;
	}
}
