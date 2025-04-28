package it.polimi.ingsw.message.server;

import it.polimi.ingsw.controller.server.ServerController;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.cards.state.CardState;
import it.polimi.ingsw.model.state.GameState;
import it.polimi.ingsw.model.state.VoyageState;

public class ServerDisconnectMessage extends ServerMessage {

	@Override
	public void receive(ServerController server) throws ForbiddenCallException {
		server.disconnect(descriptor);
	}

	@Override
	public void receive(ModelInstance model) throws ForbiddenCallException {
		if(descriptor.getPlayer()==null) throw new ForbiddenCallException("Descriptor associated to message isn't bound to player");
		model.disconnect(this.descriptor.getPlayer());
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
        state.disconnect(this.descriptor.getPlayer());
    }

}
