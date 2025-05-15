package it.polimi.ingsw.message.client;

import it.polimi.ingsw.controller.client.state.ConnectedState;

//XXX remove this one.
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
