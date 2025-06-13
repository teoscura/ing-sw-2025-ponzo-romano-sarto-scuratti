package it.polimi.ingsw.controller.server.connections;

import it.polimi.ingsw.controller.server.MainServerController;
import it.polimi.ingsw.message.server.ServerMessage;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Represents a remote object with which an RMI Client is able to interface to communicate with the underlying {@link MainServerController}.
 */
public interface VirtualServer extends Remote {
	
	/**
	 * Method called by the client remotely to send a {@link ServerMessage message} to the server.
	 * 
	 * @param message {@link ServerMessage} Message received by the server.
	 * @throws RemoteException If the underlying RMI TCP channel is disrupted in any unrecoverable way.
	 */
	void receiveMessage(ServerMessage message) throws RemoteException;
}
