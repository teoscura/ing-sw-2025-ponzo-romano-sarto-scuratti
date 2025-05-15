package it.polimi.ingsw.controller.server.connections;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMISkeletonProvider extends Remote {
	VirtualServer accept(RMIClientConnection client) throws RemoteException;
}
