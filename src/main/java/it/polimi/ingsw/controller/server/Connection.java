package it.polimi.ingsw.controller.server;

import it.polimi.ingsw.message.client.ClientMessage;

public interface Connection {
    public void sendMessage(ClientMessage m);
    public void close();
}
