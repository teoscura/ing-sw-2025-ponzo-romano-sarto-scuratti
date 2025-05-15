package it.polimi.ingsw.message.server;

import it.polimi.ingsw.controller.server.LobbyController;
import it.polimi.ingsw.controller.server.MainServerController;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.cards.state.CardState;
import it.polimi.ingsw.model.components.enums.ShipmentType;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.GameState;

public class TakeCargoMessage extends ServerMessage {

	private final ShipCoords coords;
	private final ShipmentType type;

	public TakeCargoMessage(ShipCoords coords, ShipmentType type) {
		if (coords == null || type.getValue() < 1) throw new NullPointerException();
		this.coords = coords;
		this.type = type;
	}

	@Override
	public void receive(MainServerController server) throws ForbiddenCallException {
	}

	@Override
	public void receive(LobbyController server) throws ForbiddenCallException {
		if (descriptor.getPlayer() == null)
			throw new ForbiddenCallException("Descriptor associated to message isn't bound to player");
		server.getModel().validate(this);
	}

	@Override
	public void receive(ModelInstance instance) throws ForbiddenCallException {
		instance.getState().validate(this);
	}

	@Override
	public void receive(GameState state) throws ForbiddenCallException {
		state.getCardState(this.descriptor.getPlayer()).validate(this);
	}

	@Override
	public void receive(CardState state) throws ForbiddenCallException {
		state.takeCargo(this.descriptor.getPlayer(), type, coords);
	}

	public ShipCoords getCoords() {
		return coords;
	}

	public ShipmentType getShipmentType() {
		return type;
	}

}
