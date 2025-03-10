package it.polimi.ingsw.exceptions;

public class IllegalConstructorArgumentException extends RuntimeException {
    
    public IllegalConstructorArgumentException(){}

    public IllegalConstructorArgumentException(String message){
        super(message);
    }
}
