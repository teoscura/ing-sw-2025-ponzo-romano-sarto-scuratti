package it.polimi.ingsw.controller.client;

import it.polimi.ingsw.message.server.ServerMessage;

public interface ServerConnection {
    public void sendMessage(ServerMessage message);
    public void read();
    public void close();
}
