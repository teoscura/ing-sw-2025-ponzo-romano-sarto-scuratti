package it.polimi.ingsw.model.components.enums;

/**
 * Enumeration that specifies the supported types of {@link it.polimi.ingsw.model.components.CannonComponent} in this implementation of Galaxy Trucker.
 */
public enum CannonType {
	/**
	 * Single cannon, not powerable.
	 */
	SINGLE(1f, false),
	/**
	 * Double cannon, powerable.
	 */
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