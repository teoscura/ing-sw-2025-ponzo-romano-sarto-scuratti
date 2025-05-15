package it.polimi.ingsw.exceptions;

public class PlayerNotFoundException extends Exception {
	public PlayerNotFoundException() {
		super();
	}

	public PlayerNotFoundException(String message) {
		super(message);
	}
}
