package it.polimi.ingsw.controller.client.state;

import it.polimi.ingsw.controller.ThreadSafeMessageQueue;
import it.polimi.ingsw.controller.client.ClientController;
import it.polimi.ingsw.controller.client.connections.ConnectionType;
import it.polimi.ingsw.controller.client.connections.RMIConnection;
import it.polimi.ingsw.controller.client.connections.ServerConnection;
import it.polimi.ingsw.controller.client.connections.SocketConnection;
import it.polimi.ingsw.message.client.ClientMessage;
import it.polimi.ingsw.message.server.UsernameSetupMessage;
import it.polimi.ingsw.view.ClientView;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ConnectingState extends ClientControllerState {

	private final String username;
	private ServerConnection connection = null;
	private ThreadSafeMessageQueue<ClientMessage> inqueue;

	public ConnectingState(ClientController controller, ClientView view, String username) {
		super(controller, view);
		if (username == null) throw new NullPointerException();
		this.username = username;
	}

	@Override
	public void init() {
		view.show(this);
	}

	public ClientController getController() {
		return this.controller;
	}

	@Override
	public ClientControllerState getNext() {
		return new ConnectedState(controller, view, username, connection, this.inqueue);
	}

	public void connect(String address, int port, ConnectionType type) {
		this.inqueue = new ThreadSafeMessageQueue<>(100);
		switch (type) {
			case ConnectionType.RMI: {
				try {
					this.connection = new RMIConnection(inqueue, address, username, port);
				} catch (RemoteException e) {
					view.showTextMessage("Failed to connect to server, terminating.");		
				} catch (NotBoundException e) {
					view.showTextMessage("Selected server does not host game, terminating.");
				} catch (NullPointerException e) {
					view.showTextMessage("Server refused the connection... terminating.");
				}
			}
			break;
			case ConnectionType.SOCKET: {
				SocketConnection tmp = null;
				try {
					tmp = new SocketConnection(inqueue, address, port);
					tmp.start();
					tmp.sendMessage(new UsernameSetupMessage(this.username));
					connection = tmp;
				} catch (IOException e) {
					view.showTextMessage("Failed to connect to server, going back to main screen.");
				}
			}
			break;
			default:
				this.connection = null;
		}
		if (connection == null) this.controller.reset();
		else this.transition();
	}

}
