package it.polimi.ingsw.controller.client;

import it.polimi.ingsw.message.server.ServerMessage;

public interface RemoteServer {
    public void receiveMessage(ServerMessage message);
}
