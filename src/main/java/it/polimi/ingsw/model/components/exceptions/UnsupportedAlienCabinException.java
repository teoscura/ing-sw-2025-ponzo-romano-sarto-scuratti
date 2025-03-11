package it.polimi.ingsw.model.components.exceptions;

public class UnsupportedAlienCabinException extends RuntimeException{
    public UnsupportedAlienCabinException(){
        super();
    }
    public UnsupportedAlienCabinException(String message){
        super(message);
    }
}
