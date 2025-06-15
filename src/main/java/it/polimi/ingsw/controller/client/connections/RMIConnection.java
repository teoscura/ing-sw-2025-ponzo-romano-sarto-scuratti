package it.polimi.ingsw.controller.client.connections;

import it.polimi.ingsw.controller.ThreadSafeMessageQueue;
import it.polimi.ingsw.controller.server.connections.VirtualServerProvider;
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

/**
 * A client side RMI connection to a remote game server.
 */
public class RMIConnection implements ServerConnection {

	private final RMIClientStub stub;
	private final VirtualServer server;

	/**
	 * Constructs a {@link RMIConnection} object.
	 * 
	 * @param inqueue {@link ThreadSafeMessageQueue} Queue in which incoming {@link ServerMessage messages} will be inserted.
	 * @param server_ip IP address on which the remote RMI Registry is running. 
	 * @param username Username with which the client wishes to connect to the server.
	 * @param port Port on which the RMI Server is listening.
	 * @throws RemoteException If the underlying RMI TCP channel is disrupted in any unrecoverable way.
	 * @throws NotBoundException If the requested server does not have any object bound to the requested string.
	 * @throws NullPointerException If the server refused the connection.
	 */
	public RMIConnection(ThreadSafeMessageQueue<ClientMessage> inqueue, String server_ip, String username, int port) throws RemoteException, NotBoundException, NullPointerException {
		Registry registry = LocateRegistry.getRegistry(server_ip, port);
		this.stub = new RMIClientStub(inqueue, username);
		this.server = ((VirtualServerProvider) registry.lookup("galaxy_truckers")).accept(stub);
		//TODO: fix this and make the client reset.
		if (this.server == null) throw new NullPointerException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendMessage(ServerMessage message) throws RemoteException {
		this.server.receiveMessage(message);
	}

	/**
	 * {@inheritDoc}
	 */
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Thread getShutdownHook() {
		return new Thread() {
			public void run() {
				close();
			}
		};
	}

}
