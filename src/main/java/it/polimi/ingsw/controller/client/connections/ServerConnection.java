package it.polimi.ingsw.controller.client.connections;

import java.io.IOException;

import it.polimi.ingsw.message.server.ServerMessage;

public interface ServerConnection {

	void sendMessage(ServerMessage message) throws IOException;

	void close();

	Thread getShutdownHook();
}
