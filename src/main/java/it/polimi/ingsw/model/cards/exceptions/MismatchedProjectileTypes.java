package it.polimi.ingsw.model.cards.exceptions;

public class MismatchedProjectileTypes extends RuntimeException {
	public MismatchedProjectileTypes() {
		super();
	}

	public MismatchedProjectileTypes(String message) {
		super(message);
	}
}
