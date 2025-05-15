package it.polimi.ingsw.message.server;

import it.polimi.ingsw.controller.server.LobbyController;
import it.polimi.ingsw.controller.server.MainServerController;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.cards.state.CardState;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.GameState;
import it.polimi.ingsw.model.state.VoyageState;

public class SelectBlobMessage extends ServerMessage {

	private final ShipCoords blob_coord;

	public SelectBlobMessage(ShipCoords blob_coord) {
		if (blob_coord == null) throw new NullPointerException();
		this.blob_coord = blob_coord;
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
		if (state instanceof VoyageState) {
			state.getCardState(this.descriptor.getPlayer()).validate(this);
		} else {
			state.selectBlob(this.descriptor.getPlayer(), blob_coord);
		}
	}

	@Override
	public void receive(CardState state) throws ForbiddenCallException {
		state.selectBlob(this.descriptor.getPlayer(), blob_coord);
	}

	public ShipCoords getCoords() {
		return blob_coord;
	}

}
