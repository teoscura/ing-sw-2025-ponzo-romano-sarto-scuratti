package it.polimi.ingsw.controller.server.exceptions;

public class PlayerUnsetException extends RuntimeException {
    public PlayerUnsetException(){
        super();
    }
    public PlayerUnsetException(String message){
        super(message);
    }
}
