package it.polimi.ingsw.model.adventure_cards.utils;

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
