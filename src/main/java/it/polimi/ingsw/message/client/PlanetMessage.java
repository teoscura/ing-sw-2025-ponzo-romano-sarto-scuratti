package it.polimi.ingsw.message.client;

import it.polimi.ingsw.controller.client.iClientController;

public class PlanetMessage extends ClientMessage{

    private final int[] contains;
    private final int days;

    public PlanetMessage(int[] contains, int days){
        this.contains = contains;
        this.days = days;
    }

    @Override
    public void recieve(iClientController client) {
        client.moveOnBoard(-days);
        client.giveCargo(contains);
    }
    
}
