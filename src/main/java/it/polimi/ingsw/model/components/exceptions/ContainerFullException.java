package it.polimi.ingsw.model.components.exceptions;

public class ContainerFullException extends RuntimeException{
    
    public ContainerFullException(){}

    public ContainerFullException(String message){
        super(message);
    }
}