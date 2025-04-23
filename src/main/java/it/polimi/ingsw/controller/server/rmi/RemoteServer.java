package it.polimi.ingsw.controller.server.rmi;

import java.rmi.RemoteException;

import it.polimi.ingsw.message.server.ServerMessage;

public interface RemoteServer {
    public void receiveMessage(ServerMessage message) throws RemoteException;
}
