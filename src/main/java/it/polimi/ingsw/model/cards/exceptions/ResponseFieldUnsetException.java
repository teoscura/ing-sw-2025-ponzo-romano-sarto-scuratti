package it.polimi.ingsw.model.cards.exceptions;

public class ResponseFieldUnsetException extends RuntimeException {
    public ResponseFieldUnsetException(){
        super();
    }
    public ResponseFieldUnsetException(String message){
        super(message);
    }
}
