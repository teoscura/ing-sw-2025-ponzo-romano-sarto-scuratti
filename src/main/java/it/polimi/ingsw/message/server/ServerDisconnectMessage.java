package it.polimi.ingsw.message.server;

import it.polimi.ingsw.controller.server.LobbyController;
import it.polimi.ingsw.controller.server.MainServerController;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.cards.state.CardState;
import it.polimi.ingsw.model.state.GameState;

public class ServerDisconnectMessage extends ServerMessage {

	@Override
    public void receive(MainServerController server) throws ForbiddenCallException {
        server.disconnect(descriptor);
    }

	@Override
	public void receive(LobbyController server) throws ForbiddenCallException {
		server.disconnect(descriptor);
	}

	@Override
	public void receive(ModelInstance model) throws ForbiddenCallException {
		if(descriptor.getPlayer()==null) model.disconnect(descriptor);
		else model.disconnect(this.descriptor.getPlayer());
	}

	@Override
	public void receive(GameState state) throws ForbiddenCallException {
		if(this.descriptor.getPlayer()==null) state.disconnect(this.descriptor);
		else state.disconnect(this.descriptor.getPlayer());
	}

	@Override
    public void receive(CardState state) throws ForbiddenCallException {
        state.disconnect(this.descriptor.getPlayer());
    }

}
