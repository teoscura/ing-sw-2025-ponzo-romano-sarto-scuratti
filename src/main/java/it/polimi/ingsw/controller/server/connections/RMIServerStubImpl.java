package it.polimi.ingsw.controller.server.connections;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.controller.server.MainServerController;
import it.polimi.ingsw.message.server.ServerMessage;

public class RMIServerStubImpl extends UnicastRemoteObject implements VirtualServer {

	private transient final MainServerController controller;
	private transient final ClientDescriptor client;

	public RMIServerStubImpl(MainServerController controller, ClientDescriptor client) throws RemoteException {
		super();
		this.controller = controller;
		this.client = client;
	}

	public void receiveMessage(ServerMessage message) throws RemoteException {
		message.setDescriptor(this.client);
		this.controller.receiveMessage(message);
	}

}
