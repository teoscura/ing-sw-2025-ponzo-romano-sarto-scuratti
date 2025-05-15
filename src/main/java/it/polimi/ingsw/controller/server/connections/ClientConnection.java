package it.polimi.ingsw.controller.server.connections;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

import it.polimi.ingsw.message.client.ClientMessage;

public interface ClientConnection extends Remote {
	
	void sendMessage(ClientMessage m) throws IOException, RemoteException;

	void close() throws RemoteException;
}
