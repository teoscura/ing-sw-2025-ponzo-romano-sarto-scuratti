package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.server.connections.ClientConnection;
import it.polimi.ingsw.message.client.ClientMessage;

import java.io.IOException;

public class DummyConnection implements ClientConnection {

	@Override
	public void sendMessage(ClientMessage m) throws IOException {
	}

	@Override
	public void close() {
	}

}
