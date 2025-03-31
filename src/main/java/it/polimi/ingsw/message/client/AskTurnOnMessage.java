package it.polimi.ingsw.message.client;

import it.polimi.ingsw.controller.client.iClientController;

public class AskTurnOnMessage extends ClientMessage {

    @Override
    public void recieve(iClientController client) {
        client.askTurnOn();
    }

}
