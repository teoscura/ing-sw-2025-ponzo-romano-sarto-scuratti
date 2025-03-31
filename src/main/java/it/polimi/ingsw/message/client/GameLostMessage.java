package it.polimi.ingsw.message.client;

import it.polimi.ingsw.controller.client.iClientController;

public class GameLostMessage extends ClientMessage {

    @Override
    public void recieve(iClientController client) {
        client.setLost();
    }

}
