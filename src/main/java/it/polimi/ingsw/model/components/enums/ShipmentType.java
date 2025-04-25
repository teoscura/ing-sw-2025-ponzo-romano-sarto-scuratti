package it.polimi.ingsw.model.components.enums;

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

	public boolean getSpecial() {
		return this.special;
	}

	public int getValue() {
		return this.value;
	}

	static public ShipmentType[] getTypes() {
		return new ShipmentType[]{ShipmentType.BLUE, ShipmentType.GREEN, ShipmentType.YELLOW, ShipmentType.RED};
	}
}