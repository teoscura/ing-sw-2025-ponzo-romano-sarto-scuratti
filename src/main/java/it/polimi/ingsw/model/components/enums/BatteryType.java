package it.polimi.ingsw.model.components.enums;

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
