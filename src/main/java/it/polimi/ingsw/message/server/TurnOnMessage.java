package it.polimi.ingsw.message.server;

import it.polimi.ingsw.controller.server.LobbyController;
import it.polimi.ingsw.controller.server.MainServerController;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.cards.state.CardState;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.GameState;

public class TurnOnMessage extends ServerMessage {

	private final ShipCoords target;
	private final ShipCoords battery;

	public TurnOnMessage(ShipCoords target, ShipCoords battery) {
		if (target == null || battery == null) throw new NullPointerException();
		this.target = target;
		this.battery = battery;
	}

	@Override
	public void receive(MainServerController server) throws ForbiddenCallException {
		throw new ForbiddenCallException("Client: '" + this.descriptor.getUsername() + "' sent a turn on message while in lobby select");
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
		state.turnOn(this.descriptor.getPlayer(), target, battery);
	}

	public ShipCoords getTarget() {
		return target;
	}

	public ShipCoords getBattery() {
		return battery;
	}

}
