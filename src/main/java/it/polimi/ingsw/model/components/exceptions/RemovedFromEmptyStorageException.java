package it.polimi.ingsw.model.components.exceptions;

public class RemovedFromEmptyStorageException extends RuntimeException {
    
    public RemovedFromEmptyStorageException(){}

    public RemovedFromEmptyStorageException(String message){
        super(message);
    }
}
