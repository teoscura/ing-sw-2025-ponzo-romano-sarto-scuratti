package it.polimi.ingsw.model.client.state;

import it.polimi.ingsw.model.client.player.ClientVerifyPlayer;

import java.util.ArrayList;

public class ClientVerifyState implements ClientState {

	private final ArrayList<ClientVerifyPlayer> players;

	public ClientVerifyState(ArrayList<ClientVerifyPlayer> playerlist) {
		if (playerlist == null) throw new NullPointerException();
		if (playerlist.size() > 4) throw new IllegalArgumentException();
		this.players = playerlist;
	}

	public ArrayList<ClientVerifyPlayer> getPlayerList() {
		return players;
	}

	@Override
	public void sendToView(ClientStateVisitor visitor) {
		visitor.show(this);
	}

}
