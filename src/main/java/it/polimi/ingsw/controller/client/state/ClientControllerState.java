package it.polimi.ingsw.controller.client.state;

import it.polimi.ingsw.controller.client.ClientController;
import it.polimi.ingsw.controller.client.connections.ConnectionType;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.view.ClientView;

public abstract class ClientControllerState {

	protected final ClientController controller;
	protected final ClientView view;

	public ClientControllerState(ClientController controller, ClientView view) {
		if (view == null || controller == null) throw new NullPointerException();
		this.controller = controller;
		this.view = view;
	}

	public abstract void init();

	public abstract ClientControllerState getNext();

	public void onClose() {
	}

	protected void transition() {
		this.controller.setState(this.getNext());
	}

	protected ClientView getView() {
		return this.view;
	}

	public void setUsername(String username) {
		throw new RuntimeException();
	}

	public void connect(String address, int port, ConnectionType type) {
		throw new RuntimeException();
	}

	public void sendMessage(ServerMessage messsage) {
		throw new RuntimeException();
	}

	public void disconnect() {
		throw new RuntimeException();
	}

}
