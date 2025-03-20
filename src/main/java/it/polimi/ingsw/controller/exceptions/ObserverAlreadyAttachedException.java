package it.polimi.ingsw.controller.exceptions;

public class ObserverAlreadyAttachedException extends Exception {
    public ObserverAlreadyAttachedException(){
        super();
    }
    public ObserverAlreadyAttachedException(String message){
        super(message);
    }
}   
