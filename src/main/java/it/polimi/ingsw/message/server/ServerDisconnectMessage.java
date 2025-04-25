package it.polimi.ingsw.message.server;

import it.polimi.ingsw.controller.server.ServerController;
import it.polimi.ingsw.model.adventure_cards.exceptions.ForbiddenCallException;

public class ServerDisconnectMessage extends ServerMessage {

	@Override
	public void receive(ServerController server) throws ForbiddenCallException {
		server.disconnect(descriptor);
	}

}
