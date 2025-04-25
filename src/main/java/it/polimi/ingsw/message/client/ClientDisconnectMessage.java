package it.polimi.ingsw.message.client;

import it.polimi.ingsw.controller.client.ClientController;

public class ClientDisconnectMessage extends ClientMessage {

    @Override
    public void receive(ClientController client) {
        client.disconnect();
    }

}
