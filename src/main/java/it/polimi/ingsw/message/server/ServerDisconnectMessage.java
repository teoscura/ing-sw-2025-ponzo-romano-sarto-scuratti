package it.polimi.ingsw.message.server;

import it.polimi.ingsw.controller.server.ServerController;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.cards.state.CardState;

public class ServerDisconnectMessage extends ServerMessage {

	@Override
	public void receive(ServerController server) throws ForbiddenCallException {
		server.disconnect(descriptor);
	}

	@Override
    public void receive(CardState state) throws ForbiddenCallException {
        state.disconnect(this.descriptor.getPlayer());
    }

}
