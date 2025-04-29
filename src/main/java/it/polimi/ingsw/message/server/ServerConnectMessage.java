package it.polimi.ingsw.message.server;

import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.controller.server.ServerController;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.state.GameState;

public class ServerConnectMessage extends ServerMessage {

	public ServerConnectMessage(ClientDescriptor client) {
		if (client == null) throw new NullPointerException();
		this.descriptor = client;
	}

	@Override
	public void receive(ServerController server) throws ForbiddenCallException {
		server.connect(descriptor);
	}

	@Override
	public void receive(GameState state) throws ForbiddenCallException {
		if(this.descriptor.getPlayer()==null) state.connect(descriptor);
		else state.connect(this.descriptor.getPlayer());
	}

}
