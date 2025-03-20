package it.polimi.ingsw.controller.exceptions;

public class UserAlreadyInGameException extends Exception {
    public UserAlreadyInGameException(){
        super();
    }
    public UserAlreadyInGameException(String message){
        super(message);
    }
}
