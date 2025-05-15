package it.polimi.ingsw.controller.server.connections;

import java.io.IOException;

import it.polimi.ingsw.message.client.ClientMessage;

public interface Connection {
	
	void sendMessage(ClientMessage m) throws IOException;

	void close();
}
