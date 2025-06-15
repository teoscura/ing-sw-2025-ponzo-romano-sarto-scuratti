package it.polimi.ingsw.message.server;

import it.polimi.ingsw.controller.server.MainServerController;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;

/**
 * Server message indicating a request by a client to leave lobby creation.
 */
public class LeaveSetupMessage extends ServerMessage {

	@Override
	public void receive(MainServerController server) throws ForbiddenCallException {
		server.leaveSetup(descriptor);
	}

}
