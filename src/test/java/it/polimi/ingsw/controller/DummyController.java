package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.server.LobbyController;
import it.polimi.ingsw.message.client.ClientMessage;

public class DummyController extends LobbyController {

	public DummyController(int id) {
		super(id);
	}

	@Override
	public void endGame() {
	}

	@Override
	public void serializeCurrentGame() {
	}

	@Override
	public void broadcast(ClientMessage message) {
	}
}
