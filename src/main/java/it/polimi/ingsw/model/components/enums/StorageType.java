package it.polimi.ingsw.model.components.enums;


/**
 * Enumeration that specifies the supported types of {@link it.polimi.ingsw.model.components.StorageComponent} in this implementation of Galaxy Trucker.
 */
public enum StorageType {
	DOUBLENORMAL(false, 2),
	TRIPLENORMAL(false, 3),
	SINGLESPECIAL(true, 1),
	DOUBLESPECIAL(true, 2);

	private final boolean special;
	private final int capacity;

	StorageType(boolean special, int capacity) {
		this.special = special;
		this.capacity = capacity;
	}

	public boolean getSpecial() {
		return this.special;
	}

	public int getCapacity() {
		return this.capacity;
	}
}