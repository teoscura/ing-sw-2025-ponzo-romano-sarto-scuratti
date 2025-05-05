package it.polimi.ingsw.message.server;

import it.polimi.ingsw.controller.server.MainServerController;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;

public class PingMessage extends ServerMessage {

	@Override
	public void receive(MainServerController server) throws ForbiddenCallException {
		server.ping(this.descriptor);
	}

}
