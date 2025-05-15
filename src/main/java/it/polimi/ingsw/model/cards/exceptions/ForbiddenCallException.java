package it.polimi.ingsw.model.cards.exceptions;

public class ForbiddenCallException extends Exception {
	public ForbiddenCallException() {
		super();
	}

	public ForbiddenCallException(String message) {
		super(message);
	}
}
