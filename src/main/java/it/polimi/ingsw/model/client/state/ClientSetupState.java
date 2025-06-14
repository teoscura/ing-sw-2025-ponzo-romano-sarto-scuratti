package it.polimi.ingsw.model.client.state;

import it.polimi.ingsw.model.client.ClientGameListEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Client side representation of the lobby creation screen.
 */
public class ClientSetupState implements ClientState {

	private final String setupper_username;
	private final ArrayList<ClientGameListEntry> unfinished_games;

	public ClientSetupState(String setupper_username, List<ClientGameListEntry> unfinished_games) {
		if (setupper_username == null || unfinished_games == null) throw new NullPointerException();
		this.setupper_username = setupper_username;
		this.unfinished_games = new ArrayList<>(unfinished_games);
	}

	public String getSetupperName() {
		return this.setupper_username;
	}

	public ArrayList<ClientGameListEntry> getUnfinishedList() {
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
