package it.polimi.ingsw.model.components.exceptions;

public class ComponentNotEmptyException extends RuntimeException {
	public ComponentNotEmptyException() {
		super();
	}

	public ComponentNotEmptyException(String message) {
		super(message);
	}
}
