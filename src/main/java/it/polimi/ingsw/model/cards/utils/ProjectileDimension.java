package it.polimi.ingsw.model.cards.utils;

/**
 * Enumeration representing the dimensions of a {@link Projectile}.
 */
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
