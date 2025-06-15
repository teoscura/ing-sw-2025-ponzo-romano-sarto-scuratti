package it.polimi.ingsw.controller.client.connections;

import it.polimi.ingsw.message.server.ServerMessage;

import java.io.IOException;

/*
 * A client side connection to a remote game server.
 */
public interface ServerConnection {

	/**
	 * Send a {@link it.polimi.ingsw.message.server.ServerMessage} to the server according to the underlying network implementation.
	 * @param message {@link it.polimi.ingsw.message.server.ServerMessage} Message to be sent.
	 * @throws IOException If there are any errors during the transfer process.
	 */
	void sendMessage(ServerMessage message) throws IOException;
	
	/**
	 * Close the connection.
	 */
	void close();

	/**
	 * @return A Thread object in charge of cleaning up any connection related resources at shutdown. 
	 */
	Thread getShutdownHook();
}
