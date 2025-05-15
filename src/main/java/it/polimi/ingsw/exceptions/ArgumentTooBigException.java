package it.polimi.ingsw.exceptions;

public class ArgumentTooBigException extends RuntimeException {
	public ArgumentTooBigException() {
	}

	public ArgumentTooBigException(String message) {
		super(message);
	}
}
