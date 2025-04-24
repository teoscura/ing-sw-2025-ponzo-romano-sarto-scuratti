package it.polimi.ingsw.model;

public enum PlayerCount {
	TWO(2),
	THREE(3),
	FOUR(4);

	private final int number;

	PlayerCount(int number) {
		this.number = number;
	}

	public int getNumber() {
		return this.number;
	}
}
