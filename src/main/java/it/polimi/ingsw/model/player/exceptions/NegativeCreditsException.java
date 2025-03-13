package it.polimi.ingsw.model.player.exceptions;

public class NegativeCreditsException extends RuntimeException{
    public NegativeCreditsException(){
        super();
    }

    public NegativeCreditsException(String message){
        super(message);
    }
}
