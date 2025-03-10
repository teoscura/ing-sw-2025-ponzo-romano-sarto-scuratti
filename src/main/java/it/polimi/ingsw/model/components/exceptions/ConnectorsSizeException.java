package it.polimi.ingsw.model.components.exceptions;

public class ConnectorsSizeException extends RuntimeException{
    
    public ConnectorsSizeException(){}

    public ConnectorsSizeException(String message){
        super(message);
    }
}

