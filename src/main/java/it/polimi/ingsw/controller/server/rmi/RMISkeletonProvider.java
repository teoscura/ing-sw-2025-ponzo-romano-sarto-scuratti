package it.polimi.ingsw.controller.server.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import it.polimi.ingsw.controller.client.RMIClientStub;

public interface RMISkeletonProvider extends Remote {

    public RMIServerSkeleton accept(RMIClientStub client) throws RemoteException;

}
