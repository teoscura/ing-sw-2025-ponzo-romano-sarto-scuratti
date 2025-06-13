package it.polimi.ingsw.controller.server.connections;

import java.rmi.Remote;
import java.rmi.RemoteException;

import it.polimi.ingsw.controller.server.MainServerController;

/**
 * Represents an object capable of providing a VirtualServer implementation.
 */
public interface VirtualServerProvider extends Remote {
	
	/**
	 * Requests a VirtualServer instance to the {@link RMIClientConnection} requesting it.
	 * 
	 * @param client {@link RMIClientConnection} RMI Stub of the client requesting the VirtualServer reference.
	 * @return {@link VirtualServer} An instance of the VirtualServer interface of the underlying {@link MainServerController}, or null if the connection is refused.
	 * @throws RemoteException If the underlying RMI TCP channel is disrupted in any unrecoverable way.
	 */
	VirtualServer accept(RMIClientConnection client) throws RemoteException;
}
