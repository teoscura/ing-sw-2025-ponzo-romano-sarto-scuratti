package it.polimi.ingsw.model.client.state;

import it.polimi.ingsw.model.client.ClientGameListEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Client side representation of the lobby selection screen.
 */
public class ClientLobbySelectState implements ClientState {

	private final ArrayList<ClientGameListEntry> unfinished_games;

	public ClientLobbySelectState(List<ClientGameListEntry> unfinished_games) {
		if (unfinished_games == null) throw new NullPointerException();
		this.unfinished_games = new ArrayList<>(unfinished_games);
	}

	public ArrayList<ClientGameListEntry> getLobbyList() {
		return this.unfinished_games;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendToView(ClientStateVisitor visitor) {
		visitor.show(this);
	}

}
