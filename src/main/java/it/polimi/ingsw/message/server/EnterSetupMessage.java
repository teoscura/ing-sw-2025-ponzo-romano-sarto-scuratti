package it.polimi.ingsw.message.server;

import it.polimi.ingsw.controller.server.MainServerController;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;

/**
 * Server message indicating a request by a client to enter lobby creation.
 */
public class EnterSetupMessage extends ServerMessage {

	@Override
	public void receive(MainServerController server) throws ForbiddenCallException {
		server.enterSetup(descriptor);
	}

}
