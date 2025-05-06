package it.polimi.ingsw.message.server;

import java.io.Serializable;

import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.controller.server.LobbyController;
import it.polimi.ingsw.controller.server.MainServerController;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.cards.state.CardState;
import it.polimi.ingsw.model.state.GameState;

public abstract class ServerMessage implements Serializable {

	protected transient ClientDescriptor descriptor = null;

	public void setDescriptor(ClientDescriptor client) {
		this.descriptor = client;
	}

	public ClientDescriptor getDescriptor() {
		return this.descriptor;
	}

	//All messages need to know what to do when initially executed.
	public abstract void receive(MainServerController server) throws ForbiddenCallException;

	//Can be omitted
	public void receive(LobbyController lobby) throws ForbiddenCallException {}

	//Can be omitted
	public void receive(ModelInstance instance) throws ForbiddenCallException {}

	//Can be omitted
	public void receive(GameState state) throws ForbiddenCallException {}

	//Can be omitted
	public void receive(CardState state) throws ForbiddenCallException {}


}
