package it.polimi.ingsw.message.server;

import it.polimi.ingsw.controller.server.LobbyController;
import it.polimi.ingsw.controller.server.MainServerController;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.state.GameState;

public class TakeDiscardedComponentMessage extends ServerMessage {

	private final int id;

	public TakeDiscardedComponentMessage(int id) {
		if (id <= 0 || id > 156) throw new IllegalArgumentException();
		this.id = id;
	}

	@Override
    public void receive(MainServerController server) throws ForbiddenCallException {
        return;
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
		state.takeDiscarded(this.descriptor.getPlayer(), id);
	}

	public int getId() {
		return id;
	}

}
