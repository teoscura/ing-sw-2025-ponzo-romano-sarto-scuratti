package it.polimi.ingsw.exceptions;

public class NotUniqueException extends RuntimeException{
    public NotUniqueException(){
        super();
    }

    public NotUniqueException(String message){
        super(message);
    }
}
