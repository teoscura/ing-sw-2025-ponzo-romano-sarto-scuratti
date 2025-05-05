package it.polimi.ingsw.message.server;

import it.polimi.ingsw.controller.server.LobbyController;
import it.polimi.ingsw.controller.server.MainServerController;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.GameState;

public class PutComponentMessage extends ServerMessage {

	private final ShipCoords coords;
	private final ComponentRotation rotation;

	public PutComponentMessage(ShipCoords coords, ComponentRotation rotation) {
		if (coords == null)
			throw new NullPointerException();
		this.coords = coords;
		this.rotation = rotation;
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
		state.putComponent(this.descriptor.getPlayer(), coords, rotation);
	}

	public ShipCoords getCoords() {
		return coords;
	}

	public ComponentRotation getRotation() {
		return rotation;
	}

}
