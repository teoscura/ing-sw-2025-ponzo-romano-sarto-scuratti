package it.polimi.ingsw.controller.client.connections;

import it.polimi.ingsw.message.server.ServerMessage;

import java.io.IOException;

public interface ServerConnection {

	void sendMessage(ServerMessage message) throws IOException;

	void close();

	Thread getShutdownHook();
}
