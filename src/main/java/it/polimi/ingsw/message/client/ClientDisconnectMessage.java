package it.polimi.ingsw.message.client;

import it.polimi.ingsw.controller.client.state.ConnectedState;

/**
 * Client message forcing a disconnection from the remote game server.
 */
public class ClientDisconnectMessage extends ClientMessage {

	@Override
	public void receive(ConnectedState client) {
		client.disconnect();
	}

}
