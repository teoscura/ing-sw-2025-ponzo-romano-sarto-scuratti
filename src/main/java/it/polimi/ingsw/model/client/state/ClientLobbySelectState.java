package it.polimi.ingsw.model.client.state;

import it.polimi.ingsw.model.client.ClientGameListEntry;
import it.polimi.ingsw.view.ClientView;

import java.util.ArrayList;
import java.util.List;

public class ClientLobbySelectState implements ClientState {

	private final ArrayList<ClientGameListEntry> unfinished_games;

	public ClientLobbySelectState(List<ClientGameListEntry> unfinished_games) {
		if (unfinished_games == null) throw new NullPointerException();
		this.unfinished_games = new ArrayList<>(unfinished_games);
	}

	public ArrayList<ClientGameListEntry> getLobbyList() {
		return this.unfinished_games;
	}

	@Override
	public void sendToView(ClientView view) {
		view.show(this);
	}

}
