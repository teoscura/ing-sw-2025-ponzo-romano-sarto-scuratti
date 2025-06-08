package it.polimi.ingsw.controller.client.connections;

import it.polimi.ingsw.controller.ThreadSafeMessageQueue;
import it.polimi.ingsw.controller.server.connections.RMISkeletonProvider;
import it.polimi.ingsw.controller.server.connections.VirtualServer;
import it.polimi.ingsw.message.client.ClientMessage;
import it.polimi.ingsw.message.server.ServerDisconnectMessage;
import it.polimi.ingsw.message.server.ServerMessage;

import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMIConnection implements ServerConnection {

	private final RMIClientStub stub;
	private final VirtualServer server;

	public RMIConnection(ThreadSafeMessageQueue<ClientMessage> queue, String server_ip, String username, int port) throws RemoteException, NotBoundException, NullPointerException {
		Registry registry = LocateRegistry.getRegistry(server_ip, port);
		this.stub = new RMIClientStub(queue, username);
		this.server = ((RMISkeletonProvider) registry.lookup("galaxy_truckers")).accept(stub);
		if (this.server == null) throw new NullPointerException();
	}

	@Override
	public void sendMessage(ServerMessage message) throws RemoteException {
		this.server.receiveMessage(message);
	}

	@Override
	public void close() {
		try {
			sendMessage(new ServerDisconnectMessage());
			UnicastRemoteObject.unexportObject(stub, true);
		} catch (NoSuchObjectException e) {
			System.out.println("Unexported RMIClientStub");
		} catch (RemoteException e) {
			System.out.println("Closed RMI Connection.");
		}
	}

	@Override
	public Thread getShutdownHook() {
		return new Thread() {
			public void run() {
				close();
			}
		};
	}


}
