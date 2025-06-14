package it.polimi.ingsw.model.components.enums;

/**
 * Enumeration that specifies the supported types of cargo in this implementation of Galaxy Trucker.
 */
public enum ShipmentType {
	RED(true, 4),     //4 - Special
	YELLOW(false, 3),  //3 - Normal
	GREEN(false, 2),   //2 - Normal
	BLUE(false, 1),    //1 - Normal
	EMPTY(false, 0);  //0 - Empty Space

	private final boolean special;
	private final int value;

	ShipmentType(boolean special, int value) {
		this.special = special;
		this.value = value;
	}

	static public ShipmentType fromValue(int i) {
		switch (i) {
			case 4:
				return ShipmentType.RED;
			case 3:
				return ShipmentType.YELLOW;
			case 2:
				return ShipmentType.GREEN;
			case 1:
				return ShipmentType.BLUE;
			default:
				return ShipmentType.EMPTY;
		}
	}

	public boolean getSpecial() {
		return this.special;
	}

	public int getValue() {
		return this.value;
	}
}