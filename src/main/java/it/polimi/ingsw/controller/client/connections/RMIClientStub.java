package it.polimi.ingsw.controller.client.connections;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import it.polimi.ingsw.controller.ThreadSafeMessageQueue;
import it.polimi.ingsw.controller.server.connections.Connection;
import it.polimi.ingsw.message.client.ClientMessage;

public class RMIClientStub implements Remote, Connection {

	private transient final ThreadSafeMessageQueue<ClientMessage> inqueue;
	private final String username;

	public RMIClientStub(ThreadSafeMessageQueue<ClientMessage> inqueue, String username, int port) throws RemoteException, NotBoundException {
		if (inqueue == null || username == null) throw new NullPointerException();
		this.inqueue = inqueue;
		this.username = username;
		UnicastRemoteObject.exportObject(this, port);
	}

	@Override
	public void sendMessage(ClientMessage message) {
		inqueue.insert(message);
	}

	public String getUsername() {
		return this.username;
	}

	@Override
	public void close() {
	}

}
