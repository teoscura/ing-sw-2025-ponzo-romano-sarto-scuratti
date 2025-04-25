package it.polimi.ingsw.model.cards.exceptions;

public class CrewSizeException extends RuntimeException{
    public CrewSizeException(){
        super();
    }
    public CrewSizeException(String message){
        super(message);
    }
}
