package it.polimi.ingsw.exceptions;

public class NotPresentException extends RuntimeException {
    public NotPresentException(){
        super();
    }
    public NotPresentException(String message){
        super(message);
    }
}
