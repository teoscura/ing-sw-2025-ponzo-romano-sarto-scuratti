package it.polimi.ingsw.controller.server.connections;

import it.polimi.ingsw.message.server.ServerMessage;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualServer extends Remote {
	void receiveMessage(ServerMessage message) throws RemoteException;
}
