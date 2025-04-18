package it.polimi.ingsw.controller.client;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import it.polimi.ingsw.controller.server.Connection;
import it.polimi.ingsw.controller.server.rmi.RMISkeletonProvider;
import it.polimi.ingsw.message.client.ClientMessage;

public class RMIClientStub implements Remote, Connection {
    
    private final ClientController controller;
    private final String username;

    public RMIClientStub(ClientController controller, String username) throws RemoteException, NotBoundException{
        if(controller == null || username==null) throw new NullPointerException();
        this.controller = controller;
        this.username = username;
        UnicastRemoteObject.exportObject(this, 9999);
    }

    @Override
    public void sendMessage(ClientMessage message){
        controller.receiveMessage(message);
    }

    public String getUsername() {
        return this.username;
    }

    @Override
    public void close() {}
    
}
