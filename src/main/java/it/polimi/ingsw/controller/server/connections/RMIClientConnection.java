package it.polimi.ingsw.controller.server.connections;

import java.rmi.RemoteException;

public interface RMIClientConnection extends ClientConnection {

    public String getUsername() throws RemoteException;
    
}
