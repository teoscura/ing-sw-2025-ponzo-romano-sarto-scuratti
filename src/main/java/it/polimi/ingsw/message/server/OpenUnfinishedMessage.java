package it.polimi.ingsw.message.server;

import it.polimi.ingsw.controller.server.MainServerController;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;

/**
 * Server message indicating a request by a client to open a lobby from a save file.
 */
public class OpenUnfinishedMessage extends ServerMessage {

	private final int id;

	public OpenUnfinishedMessage(int id) {
		if (id < -1) throw new IllegalArgumentException();
		this.id = id;
	}

	@Override
	public void receive(MainServerController server) throws ForbiddenCallException {
		server.openUnfinished(descriptor, id);
	}

}
