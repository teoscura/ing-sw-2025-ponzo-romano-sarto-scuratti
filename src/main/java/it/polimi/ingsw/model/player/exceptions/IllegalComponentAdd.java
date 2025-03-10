package it.polimi.ingsw.model.player.exceptions;

public class IllegalComponentAdd extends RuntimeException{
    public IllegalComponentAdd(){
        super();
    }

    public IllegalComponentAdd(String message){
        super(message);
    }
}
