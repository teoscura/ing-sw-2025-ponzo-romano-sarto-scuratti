package it.polimi.ingsw.message.server;

import it.polimi.ingsw.controller.server.LobbyController;
import it.polimi.ingsw.controller.server.MainServerController;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;

/**
 * Server message indicating a ping from a client.
 */
public class PingMessage extends ServerMessage {

	@Override
	public void receive(MainServerController server) throws ForbiddenCallException {
		server.ping(this.descriptor);
	}

	@Override
	public void receive(LobbyController server) {
		server.ping(this.descriptor);
	}

}
