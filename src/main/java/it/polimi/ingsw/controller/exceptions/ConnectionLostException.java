package it.polimi.ingsw.controller.exceptions;

public class ConnectionLostException extends RuntimeException{
    public ConnectionLostException(){
        super();
    }
    public ConnectionLostException(String message){
        super(message);
    }
}
