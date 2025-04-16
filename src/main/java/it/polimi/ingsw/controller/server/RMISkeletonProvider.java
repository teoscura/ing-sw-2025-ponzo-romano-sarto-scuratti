package it.polimi.ingsw.controller.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import it.polimi.ingsw.controller.client.RMIServerSkeleton;

public interface RMISkeletonProvider extends Remote {

    public RMIServerSkeleton accept(RMIClientStub client) throws RemoteException;

}
