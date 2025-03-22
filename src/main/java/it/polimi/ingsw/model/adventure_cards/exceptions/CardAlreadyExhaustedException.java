package it.polimi.ingsw.model.adventure_cards.exceptions;

public class CardAlreadyExhaustedException extends RuntimeException{
    public CardAlreadyExhaustedException(){
        super();
    }
    public CardAlreadyExhaustedException(String message){
        super(message);
    }
}
