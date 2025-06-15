package it.polimi.ingsw.controller.server.connections;

import it.polimi.ingsw.message.client.ClientMessage;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface representing any possible Server-side connection to a remote Client actor.
 */
public interface ClientConnection extends Remote {

	/**
	 * Send a {@link ClientMessage} to a client according to the underlying network implementation.
	 * @param m {@link ClientMessage} Message to be sent.
	 * @throws IOException If there are any errors during the transfer process.
	 */
	void sendMessage(ClientMessage m) throws IOException;

	/**
	 * Close the connection.
	 * 
	 * @throws RemoteException If the underlying RMI TCP channel is disrupted in any unrecoverable way.
	 */
	void close() throws RemoteException;
}
