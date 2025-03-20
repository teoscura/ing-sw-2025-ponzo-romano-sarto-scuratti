package it.polimi.ingsw.model.adventure_cards;

public class ResponseFieldUnsetException extends RuntimeException {
    public ResponseFieldUnsetException(){
        super();
    }
    public ResponseFieldUnsetException(String message){
        super(message);
    }
}
