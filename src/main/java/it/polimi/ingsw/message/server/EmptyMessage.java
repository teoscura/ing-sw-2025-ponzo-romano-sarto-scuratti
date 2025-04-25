package it.polimi.ingsw.message.server;

import it.polimi.ingsw.controller.server.ServerController;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.cards.state.CardState;
import it.polimi.ingsw.model.state.GameState;

public class EmptyMessage extends ServerMessage {

	@Override
	public void receive(ServerController server) throws ForbiddenCallException {
		server.getModel().validate(this);
	}

	@Override
	public void receive(ModelInstance instance) throws ForbiddenCallException {
		instance.validate(this);
	}

	@Override
	public void receive(GameState state) throws ForbiddenCallException {
		state.validate(this);
	}

	@Override
	public void receive(CardState state) throws ForbiddenCallException {
	}

}
