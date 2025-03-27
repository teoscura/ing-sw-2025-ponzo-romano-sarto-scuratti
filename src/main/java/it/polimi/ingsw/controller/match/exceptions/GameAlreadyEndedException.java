package it.polimi.ingsw.controller.match.exceptions;

public class GameAlreadyEndedException extends Exception {
    public GameAlreadyEndedException(){
        super();
    }
    public GameAlreadyEndedException(String message){
        super(message);
    }
}