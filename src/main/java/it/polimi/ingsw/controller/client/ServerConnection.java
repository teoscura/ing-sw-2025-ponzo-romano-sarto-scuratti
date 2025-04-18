package it.polimi.ingsw.controller.client;

import java.io.IOException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import it.polimi.ingsw.message.server.ServerMessage;

public interface ServerConnection {
    public void connect(String server_ip) throws AccessException, RemoteException, NotBoundException, IOException;
    public void sendMessage(ServerMessage message);
    public void read();
    public void close();
}
