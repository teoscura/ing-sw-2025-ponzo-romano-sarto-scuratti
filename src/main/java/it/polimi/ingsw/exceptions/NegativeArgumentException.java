package it.polimi.ingsw.exceptions;

public class NegativeArgumentException extends RuntimeException{
        
    public NegativeArgumentException(){}

    public NegativeArgumentException(String message){
        super(message);
    }
}
