package it.polimi.ingsw.controller.client;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import it.polimi.ingsw.controller.server.rmi.RMIServerSkeleton;
import it.polimi.ingsw.controller.server.rmi.RMISkeletonProvider;
import it.polimi.ingsw.message.server.ServerMessage;

public class RMIConnection implements ServerConnection {

    private final RMIClientStub stub;
    private RMIServerSkeleton skeleton = null;

    public RMIConnection(ClientController controller, String server_ip, String username) throws RemoteException, NotBoundException {
        this.stub = new RMIClientStub(controller, username);
    }

    @Override
    public void connect(String server_ip) throws AccessException, RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry("localhost", 9999);
        this.skeleton =  ((RMISkeletonProvider)registry.lookup("galaxy_truckers")).accept(stub); 
   
    }

    @Override
    public void sendMessage(ServerMessage message) {
        this.skeleton.sendMessage(message);
    }

    @Override
    public void read() {}

    @Override
    public void close() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'close'");
    }
    
}
