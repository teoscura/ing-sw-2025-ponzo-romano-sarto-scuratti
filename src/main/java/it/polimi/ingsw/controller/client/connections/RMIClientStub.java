package it.polimi.ingsw.controller.client.connections;

import it.polimi.ingsw.controller.ThreadSafeMessageQueue;
import it.polimi.ingsw.controller.server.connections.RMIClientConnection;
import it.polimi.ingsw.message.client.ClientDisconnectMessage;
import it.polimi.ingsw.message.client.ClientMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.LoggerLevel;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Stub exported by the client in order for the server to have a way to communicate with the client.
 */
public class RMIClientStub extends UnicastRemoteObject implements RMIClientConnection {

	private transient final ThreadSafeMessageQueue<ClientMessage> inqueue;
	private final String username;

	/**
	 * Constructs and exports a {@link RMIClientStub} object.
	 * 
	 * @param inqueue {@link ThreadSafeMessageQueue} Queue in which incoming {@link ServerMessage messages} will be inserted.
	 * @param username Username with which the client wishes to connect to the server.
	 * @throws RemoteException If the underlying RMI TCP channel is disrupted in any unrecoverable way.
	 */
	public RMIClientStub(ThreadSafeMessageQueue<ClientMessage> inqueue, String username) throws RemoteException {
		if (inqueue == null || username == null) throw new NullPointerException();
		this.inqueue = inqueue;
		this.username = username;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendMessage(ClientMessage message) {
		inqueue.insert(message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getUsername() {
		return this.username;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() {
		try {
			this.sendMessage(new ClientDisconnectMessage());
		} catch (Throwable t) {
		} finally {
			Logger.getInstance().print(LoggerLevel.NOTIF, "Finalized closing procedure for RMI User: '" + username + "'.");
		}

	}

}
