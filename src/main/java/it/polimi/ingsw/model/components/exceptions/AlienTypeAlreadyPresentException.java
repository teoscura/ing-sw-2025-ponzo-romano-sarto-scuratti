package it.polimi.ingsw.model.components.exceptions;

public class AlienTypeAlreadyPresentException extends RuntimeException {
    public AlienTypeAlreadyPresentException(){
        super();
    }
    public AlienTypeAlreadyPresentException(String message){
        super(message);
    }
}
