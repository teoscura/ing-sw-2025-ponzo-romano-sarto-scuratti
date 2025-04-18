package it.polimi.ingsw.controller.server;

import java.io.IOException;

import it.polimi.ingsw.message.client.ClientMessage;

public interface Connection {
    public void sendMessage(ClientMessage m) throws IOException;
    public void close();
}
