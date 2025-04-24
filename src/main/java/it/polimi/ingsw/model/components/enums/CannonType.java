package it.polimi.ingsw.model.components.enums;

public enum CannonType {
	SINGLE(1, false),
	DOUBLE(2, true);

	private final int max_power;
	private final boolean powerable;

	CannonType(int max_power, boolean powerable) {
		this.max_power = max_power;
		this.powerable = powerable;
	}

	public int getMaxPower() {
		return this.max_power;
	}

	public boolean getPowerable() {
		return this.powerable;
	}

}