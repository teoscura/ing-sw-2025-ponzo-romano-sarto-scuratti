package it.polimi.ingsw.controller.server;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import it.polimi.ingsw.controller.client.ClientController;
import it.polimi.ingsw.message.client.ClientMessage;

public class RMIClientStub implements Remote, Connection {
    
    private final ClientController controller;
    private final String username;

    public RMIClientStub(ClientController controller, String username) throws RemoteException, NotBoundException{
        if(controller == null || username==null) throw new NullPointerException();
        this.controller = controller;
        this.username = username;
        Registry registry = LocateRegistry.getRegistry("localhost", 9999);
        UnicastRemoteObject.exportObject(this, 9999);
        controller.setServerConnection((((RMISkeletonProvider) registry.lookup("galaxy_truckers"))).accept(this));
    }

    @Override
    public void sendMessage(ClientMessage message){
        controller.recieveMessage(message);
    }

    public String getUsername() {
        return this.username;
    }

    @Override
    public void close() {}
    
}
