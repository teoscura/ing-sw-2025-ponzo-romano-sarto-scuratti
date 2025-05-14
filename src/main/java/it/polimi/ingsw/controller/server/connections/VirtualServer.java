package it.polimi.ingsw.controller.server.connections;

import java.rmi.Remote;
import java.rmi.RemoteException;

import it.polimi.ingsw.message.server.ServerMessage;

public interface VirtualServer extends Remote {
	void receiveMessage(ServerMessage message) throws RemoteException;
}
