package it.polimi.ingsw.message.client;

import it.polimi.ingsw.controller.client.iClientController;

public class NotifyCardStateUpdateMessage extends ClientMessage {

    @Override
    public void receive(iClientController client) {
        client.notifyCard();
    }
}
