package it.polimi.ingsw.controller;

import java.io.IOException;

import it.polimi.ingsw.controller.server.connections.ClientConnection;
import it.polimi.ingsw.message.client.ClientMessage;

public class DummyConnection implements ClientConnection {

    @Override
    public void sendMessage(ClientMessage m) throws IOException {
        return;
    }

    @Override
    public void close() {
        return;
    }
    
}
