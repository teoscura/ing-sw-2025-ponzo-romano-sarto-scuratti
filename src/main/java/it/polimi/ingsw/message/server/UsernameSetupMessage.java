package it.polimi.ingsw.message.server;

import it.polimi.ingsw.controller.server.MainServerController;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;

/**
 * Server message used during the TCP connection procedure, notifying the server about the username of the incoming connection.
 */
public class UsernameSetupMessage extends ServerMessage {

	private final String username;

	public UsernameSetupMessage(String username) {
		if (username == null) throw new NullPointerException();
		this.username = username;
	}

	public String getUsername() {
		return this.username;
	}

	@Override
	public void receive(MainServerController server) throws ForbiddenCallException {
		throw new ForbiddenCallException("Cannot setup username after finishing to connect!");
	}

}
