package it.polimi.ingsw.exceptions;

public class OutOfBoundsException extends RuntimeException{
    public OutOfBoundsException(){
        super();
    }
    public OutOfBoundsException(String message){
        super(message);
    }
}
