//Done.
package it.polimi.ingsw.model.components.enums;

/**
 * Enumeration that specifies the supported crew types in this implementation of Galaxy Trucker.
 */
public enum AlienType {
	NONE(0, false, -1),
	HUMAN(2, false, 0),
	BROWN(1, true, 1),
	PURPLE(1, true, 2),
	BOTH(1, false, -1);

	private final int max_capacity;
	private final boolean lifesupport_exists;
	private final int arraypos;

	AlienType(int max_capacity, boolean lifesupport_exists, int arraypos) {
		this.max_capacity = max_capacity;
		this.lifesupport_exists = lifesupport_exists;
		this.arraypos = arraypos;
	}

	public int getMaxCapacity() {
		return this.max_capacity;
	}

	public boolean getLifeSupportExists() {
		return this.lifesupport_exists;
	}

	public int getArraypos() {
		return this.arraypos;
	}

	/**
	 * @param type {@link AlienType} Type to confront with this.
	 * @return Whether they are compatible or not.
	 */
	public boolean compatible(AlienType type) {
		if (max_capacity == 0) return false;
		if (this.getArraypos() == type.getArraypos()) return true;
		if (this.getArraypos() == 0 && type.getArraypos() > 0) return false;
		return type.getArraypos() * this.getArraypos() <= 0;
	}
}
