package it.polimi.ingsw.model.components.exceptions;

public class ContainerEmptyException extends RuntimeException{
    
    public ContainerEmptyException(){}

    public ContainerEmptyException(String message){
        super(message);
    }
}
