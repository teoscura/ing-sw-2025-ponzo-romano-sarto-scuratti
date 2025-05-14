package it.polimi.ingsw.controller.client.connections;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import it.polimi.ingsw.controller.ThreadSafeMessageQueue;
import it.polimi.ingsw.controller.server.connections.RMISkeletonProvider;
import it.polimi.ingsw.controller.server.connections.VirtualServer;
import it.polimi.ingsw.message.client.ClientMessage;
import it.polimi.ingsw.message.server.ServerMessage;

public class RMIConnection implements ServerConnection {

	private final RMIClientStub stub;
	private VirtualServer server = null;

	public RMIConnection(ThreadSafeMessageQueue<ClientMessage>  queue, String server_ip, String username, int port) throws RemoteException, NotBoundException {
		this.stub = new RMIClientStub(queue, username, port);
		Registry registry = LocateRegistry.getRegistry("localhost", port);
		this.server = ((RMISkeletonProvider) registry.lookup("galaxy_truckers")).accept(stub);
	}

	@Override
	public void sendMessage(ServerMessage message) throws RemoteException {
		this.server.receiveMessage(message);
	}

	@Override
	public void close() {
	}

}
