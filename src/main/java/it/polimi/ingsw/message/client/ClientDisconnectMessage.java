package it.polimi.ingsw.message.client;

import it.polimi.ingsw.controller.client.state.ConnectedState;

public class ClientDisconnectMessage extends ClientMessage {

    @Override
    public void receive(ConnectedState client) {
        client.disconnect();
    }

}
