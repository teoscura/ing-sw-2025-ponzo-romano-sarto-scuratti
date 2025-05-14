package it.polimi.ingsw.controller.server.connections;

import java.rmi.Remote;
import java.rmi.RemoteException;

import it.polimi.ingsw.controller.client.connections.RMIClientStub;

public interface RMISkeletonProvider extends Remote {
	VirtualServer accept(RMIClientStub client) throws RemoteException;
}
