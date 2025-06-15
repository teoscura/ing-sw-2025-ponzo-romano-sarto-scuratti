package it.polimi.ingsw.controller.server.connections;

import java.rmi.RemoteException;

/**
 * Server side visible type of the stub the client exports in order to connect.
 */
public interface RMIClientConnection extends ClientConnection {

	/**
	 * @return Username tied to the stub object.
	 * @throws RemoteException If the underlying RMI TCP channel is disrupted in any unrecoverable way.
	 */
	String getUsername() throws RemoteException;

}
