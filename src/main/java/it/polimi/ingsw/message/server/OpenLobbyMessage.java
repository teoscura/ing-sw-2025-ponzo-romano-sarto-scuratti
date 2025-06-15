package it.polimi.ingsw.message.server;

import it.polimi.ingsw.controller.server.MainServerController;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;

/**
 * Server message indicating a request by a client to open a lobby.
 */
public class OpenLobbyMessage extends ServerMessage {

	private final PlayerCount count;
	private final GameModeType type;

	public OpenLobbyMessage(GameModeType type, PlayerCount count) {
		this.count = count;
		this.type = type;
	}

	@Override
	public void receive(MainServerController server) throws ForbiddenCallException {
		server.openNewRoom(descriptor, type, count);
	}

}
