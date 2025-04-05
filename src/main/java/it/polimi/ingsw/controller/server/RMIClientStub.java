package it.polimi.ingsw.controller.server;

import it.polimi.ingsw.message.client.ClientMessage;

public class RMIClientStub implements Connection {

    @Override
    public void sendMessage(ClientMessage m) {
        ..
    }

    @Override
    public void close() {
        ..
    }
    
}
