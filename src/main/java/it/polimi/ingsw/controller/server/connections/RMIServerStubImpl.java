package it.polimi.ingsw.controller.server.connections;

import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.controller.server.MainServerController;
import it.polimi.ingsw.message.server.ServerMessage;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Exported stub sent to the client in order to send {@link it.polimi.ingsw.message.server.ServerMessage messages} to the server.
 */
public class RMIServerStubImpl extends UnicastRemoteObject implements VirtualServer {

	private transient final MainServerController controller;
	private transient final ClientDescriptor client;

	/**
	 * Constructs a RMI Stub tied to a specific {@link it.polimi.ingsw.controller.server.ClientDescriptor}, allowing for the identification of the sender. 
	 * 
	 * @param controller {@link MainServerController} Controller instance to which the stub is tied.
	 * @param client {@link it.polimi.ingsw.controller.server.ClientDescriptor} Client to which this stub is tied. 
	 * @throws RemoteException If the underlying RMI TCP channel is disrupted in any unrecoverable way.
	 */
	public RMIServerStubImpl(MainServerController controller, ClientDescriptor client) throws RemoteException {
		super();
		this.controller = controller;
		this.client = client;
	}

	/**
	 * {@inheritDoc}
	 */
	public void receiveMessage(ServerMessage message) throws RemoteException {
		message.setDescriptor(this.client);
		this.controller.receiveMessage(message);
	}

}
