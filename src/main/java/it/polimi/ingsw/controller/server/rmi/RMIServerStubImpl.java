package it.polimi.ingsw.controller.server.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import it.polimi.ingsw.controller.client.ServerConnection;
import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.controller.server.ServerController;
import it.polimi.ingsw.message.server.ServerMessage;

public class RMIServerStubImpl extends UnicastRemoteObject implements RemoteServer {
    
    private transient final ServerController controller;
    private transient final ClientDescriptor client;

    public RMIServerStubImpl(ServerController controller, ClientDescriptor client) throws RemoteException{
        this.controller = controller;
        this.client = client;
        UnicastRemoteObject.exportObject(this, 0);
    }

    public void receiveMessage(ServerMessage message) throws RemoteException {
        message.setDescriptor(this.client);
        this.controller.receiveMessage(message);
    }

}
