package it.polimi.ingsw.model.cards.exceptions;

public class AlreadyVisitedException extends RuntimeException {
    public AlreadyVisitedException(){
        super();
    }
    public AlreadyVisitedException(String message){
        super(message);
    }
}
