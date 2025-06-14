package it.polimi.ingsw.model.client.state;

import it.polimi.ingsw.model.client.player.ClientVerifyPlayer;

import java.util.ArrayList;

/**
 * Client side representation of a {@link VerifyState}.
 */
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendToView(ClientStateVisitor visitor) {
		visitor.show(this);
	}

}
