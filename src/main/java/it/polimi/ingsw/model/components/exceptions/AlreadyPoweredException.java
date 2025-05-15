package it.polimi.ingsw.model.components.exceptions;

public class AlreadyPoweredException extends RuntimeException {

	public AlreadyPoweredException() {
	}

	public AlreadyPoweredException(String message) {
		super(message);
	}
}
