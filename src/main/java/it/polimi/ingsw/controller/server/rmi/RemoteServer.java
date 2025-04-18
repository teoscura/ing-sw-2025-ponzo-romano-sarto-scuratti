package it.polimi.ingsw.controller.server.rmi;

import it.polimi.ingsw.message.server.ServerMessage;

public interface RemoteServer {
    public void receiveMessage(ServerMessage message);
}
