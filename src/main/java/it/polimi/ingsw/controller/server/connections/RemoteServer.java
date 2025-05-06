package it.polimi.ingsw.controller.server.connections;

import java.rmi.RemoteException;

import it.polimi.ingsw.message.server.ServerMessage;

public interface RemoteServer {
	void receiveMessage(ServerMessage message) throws RemoteException;
}
