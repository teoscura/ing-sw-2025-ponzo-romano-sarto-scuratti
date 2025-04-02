package it.polimi.ingsw.message.client;

public class CargoMessage extends ClientMessage {
    
    private final int[] contains;

    public CargoMessage(int[] contains){
        if(contains==null||contains.length!=4) throw new IllegalArgumentException("Invalid cargo message");
        this.contains = contains;
    }
    
}
