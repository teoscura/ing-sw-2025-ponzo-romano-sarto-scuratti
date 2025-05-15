package it.polimi.ingsw.message.server;

import it.polimi.ingsw.controller.server.MainServerController;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;

public class EnterLobbyMessage extends ServerMessage {

	private final int id;

	public EnterLobbyMessage(int id) {
		if (id <= 0) throw new IllegalArgumentException();
		this.id = id;
	}

	@Override
	public void receive(MainServerController server) throws ForbiddenCallException {
		server.connectToLobby(descriptor, id);
	}

}
