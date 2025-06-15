package it.polimi.ingsw.model.cards.utils;

/**
 * Enumeration representing all possible criteria for which a {@link it.polimi.ingsw.model.cards.CombatZoneCard} may evaluate players.
 */
public enum CombatZoneCriteria {

	LEAST_CANNON(0),
	LEAST_ENGINE(1),
	LEAST_CREW(2);

	private final int number;

	CombatZoneCriteria(int number) {
		this.number = number;
	}

	public int getNumber() {
		return this.number;
	}
}
