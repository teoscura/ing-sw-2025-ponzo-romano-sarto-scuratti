package it.polimi.ingsw.model.components.enums;

/**
 * Enumeration that specifies the supported types of {@link BatteryComponent} in this implementation of Galaxy Trucker.
 */
public enum BatteryType {
	DOUBLE(2),
	TRIPLE(3);

	private final int capacity;

	BatteryType(int capacity) {
		this.capacity = capacity;
	}

	public int getCapacity() {
		return this.capacity;
	}
}
