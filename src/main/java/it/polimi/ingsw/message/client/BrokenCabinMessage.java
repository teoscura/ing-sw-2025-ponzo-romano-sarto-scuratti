package it.polimi.ingsw.message.client;

import it.polimi.ingsw.controller.client.iClientController;

public class BrokenCabinMessage extends ClientMessage {

    @Override
    public void recieve(iClientController client) {
        client.askChooseNewCabin();
    }

}
