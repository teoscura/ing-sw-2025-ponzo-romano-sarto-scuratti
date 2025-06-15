package it.polimi.ingsw.model.components.enums;

/**
 * Enumeration that specifies the supported types of {@link it.polimi.ingsw.model.components.EngineComponent} in this implementation of Galaxy Trucker.
 */
public enum EngineType {
	SINGLE(1, false),
	DOUBLE(2, true);

	private final int max_power;
	private final boolean powerable;

	EngineType(int max_power, boolean powerable) {
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