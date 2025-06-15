package it.polimi.ingsw.message.client;

import it.polimi.ingsw.controller.client.state.ConnectedState;

/**
 * Client message containing a client bound text message from the server.
 */
public class ViewMessage extends ClientMessage {

	private final String message;

	public ViewMessage(String message) {
		if (message == null) throw new NullPointerException();
		this.message = message;
	}

	@Override
	public void receive(ConnectedState client) {
		client.getView().showTextMessage(message);
	}

}
