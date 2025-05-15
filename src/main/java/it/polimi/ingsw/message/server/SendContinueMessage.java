package it.polimi.ingsw.message.server;

import it.polimi.ingsw.controller.server.LobbyController;
import it.polimi.ingsw.controller.server.MainServerController;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.cards.state.CardState;
import it.polimi.ingsw.model.state.GameState;
import it.polimi.ingsw.model.state.VoyageState;

public class SendContinueMessage extends ServerMessage {

	@Override
	public void receive(MainServerController server) throws ForbiddenCallException {
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
		if (state instanceof VoyageState) {
			state.getCardState(this.descriptor.getPlayer()).validate(this);
		} else {
			state.sendContinue(this.descriptor.getPlayer());
		}
	}

	@Override
	public void receive(CardState state) throws ForbiddenCallException {
		state.progressTurn(this.descriptor.getPlayer());
	}

}
