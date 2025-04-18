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

public class RMIServerSkeleton implements Remote {
    
    private final ServerController controller;
    private final ClientDescriptor client;

    public RMIServerSkeleton(ServerController controller, ClientDescriptor client){
        this.controller = controller;
        this.client = client;
        try{
            UnicastRemoteObject.exportObject(this, 0);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(ServerMessage message){
        message.setDescriptor(this.client);
        this.controller.receiveMessage(message);
    }

}
