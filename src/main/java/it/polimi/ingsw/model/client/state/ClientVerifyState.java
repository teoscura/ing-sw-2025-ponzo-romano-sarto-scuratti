package it.polimi.ingsw.model.client.state;

import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.client.player.ClientVerifyPlayer;

import java.util.ArrayList;

/**
 * Client side representation of a {@link it.polimi.ingsw.model.state.VerifyState}.
 */
public class ClientVerifyState implements ClientState {

	private final ArrayList<ClientVerifyPlayer> players;
	private final GameModeType type;

	public ClientVerifyState(GameModeType type, ArrayList<ClientVerifyPlayer> playerlist) {
		this.type = type;
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

	public GameModeType getType() {
		return type;
	}
}
