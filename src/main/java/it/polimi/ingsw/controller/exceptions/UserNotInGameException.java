package it.polimi.ingsw.controller.exceptions;

public class UserNotInGameException extends Exception{
    public UserNotInGameException(){
        super();
    }
    public UserNotInGameException(String message){
        super(message);
    }
}
