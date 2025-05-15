package it.polimi.ingsw.model.player.exceptions;

public class NegativeCrewException extends RuntimeException {
	public NegativeCrewException() {
		super();
	}

	public NegativeCrewException(String message) {
		super(message);
	}
}
