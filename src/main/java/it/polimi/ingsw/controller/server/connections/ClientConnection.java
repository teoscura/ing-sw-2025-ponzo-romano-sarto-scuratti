package it.polimi.ingsw.controller.server.connections;

import it.polimi.ingsw.message.client.ClientMessage;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientConnection extends Remote {

	void sendMessage(ClientMessage m) throws IOException;

	void close() throws RemoteException;
}
