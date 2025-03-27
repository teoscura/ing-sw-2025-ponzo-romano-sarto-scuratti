package it.polimi.ingsw.controller.match.exceptions;

public class GameAlreadyStartedException extends Exception {
    public GameAlreadyStartedException(){
        super();
    }
    public GameAlreadyStartedException(String message){
        super(message);
    }
}
