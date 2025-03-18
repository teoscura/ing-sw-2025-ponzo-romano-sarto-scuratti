package it.polimi.ingsw.model.adventure_cards.exceptions;

public class CrewSizeException extends RuntimeException{
    public CrewSizeException(){
        super();
    }
    public CrewSizeException(String message){
        super(message);
    }
}
