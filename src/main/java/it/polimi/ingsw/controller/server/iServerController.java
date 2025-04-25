package it.polimi.ingsw.controller.server;

import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;

public interface iServerController {

	//ServerController ones.
	void connect(ClientDescriptor client) throws ForbiddenCallException;

	void disconnect(ClientDescriptor client) throws ForbiddenCallException;

	void openRoom(ClientDescriptor client, GameModeType type, PlayerCount count) throws ForbiddenCallException;

	void getUnfinishedList(ClientDescriptor client) throws ForbiddenCallException;

	void getMyUnfinishedList(ClientDescriptor client) throws ForbiddenCallException;

	void openUnfinished(ClientDescriptor client, int id) throws ForbiddenCallException;

	void ping(ClientDescriptor client) throws ForbiddenCallException;
}
