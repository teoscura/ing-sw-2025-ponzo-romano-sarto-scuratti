package it.polimi.ingsw.controller.exceptions;

public class ObserverNotAttachedException extends Exception{
    public ObserverNotAttachedException(){
        super();
    }
    public ObserverNotAttachedException(String message){
        super(message);
    }
}
