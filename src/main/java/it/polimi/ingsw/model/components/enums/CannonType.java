package it.polimi.ingsw.model.components.enums;

public enum CannonType {
	SINGLE(1f, false),
	DOUBLE(2f, true);

	private final double max_power;
	private final boolean powerable;

	CannonType(double max_power, boolean powerable) {
		this.max_power = max_power;
		this.powerable = powerable;
	}

	public double getMaxPower() {
		return this.max_power;
	}

	public boolean getPowerable() {
		return this.powerable;
	}

}