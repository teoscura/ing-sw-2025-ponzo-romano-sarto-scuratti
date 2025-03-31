package it.polimi.ingsw.message.client;

import it.polimi.ingsw.controller.client.iClientController;

public class AskRemoveMerchMessage extends ClientMessage {

    private final int cargo_taken;

    public AskRemoveMerchMessage(int cargo_taken){
        this.cargo_taken = cargo_taken;
    }

    @Override
    public void recieve(iClientController client) {
        client.askRemoveCargo(cargo_taken);
    }

}
