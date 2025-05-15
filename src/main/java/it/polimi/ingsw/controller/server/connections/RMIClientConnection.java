package it.polimi.ingsw.controller.server.connections;

import java.rmi.RemoteException;

public interface RMIClientConnection extends ClientConnection {

	String getUsername() throws RemoteException;

}
