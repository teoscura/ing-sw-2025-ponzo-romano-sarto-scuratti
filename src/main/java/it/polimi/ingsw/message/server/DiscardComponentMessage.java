package it.polimi.ingsw.message.server;

import it.polimi.ingsw.controller.server.LobbyController;
import it.polimi.ingsw.controller.server.MainServerController;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.state.GameState;

/**
 * Server message indicating a player action regarding the discarding of a component during construction.
 */
public class DiscardComponentMessage extends ServerMessage {

	@Override
	public void receive(MainServerController server) throws ForbiddenCallException {
		throw new ForbiddenCallException("Client: '" + this.descriptor.getUsername() + "' sent a " + this.getClass().getSimpleName() + " message while in lobby select");
	}

	@Override
	public void receive(LobbyController server) throws ForbiddenCallException {
		if (this.descriptor.getPlayer() == null)
			throw new ForbiddenCallException("Descriptor associated to message isn't bound to player");
		server.getModel().validate(this);
	}

	@Override
	public void receive(ModelInstance instance) throws ForbiddenCallException {
		instance.getState().validate(this);
	}

	@Override
	public void receive(GameState state) throws ForbiddenCallException {
		state.discardComponent(this.descriptor.getPlayer());
	}

}
