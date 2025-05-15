package it.polimi.ingsw.model.client.state;

import it.polimi.ingsw.model.client.player.ClientEndgamePlayer;
import it.polimi.ingsw.view.ClientView;

import java.util.ArrayList;

public class ClientEndgameState implements ClientState {

	private final ArrayList<ClientEndgamePlayer> playerlist;

	public ClientEndgameState(ArrayList<ClientEndgamePlayer> playerlist) {
		this.playerlist = playerlist;
	}

	public ArrayList<ClientEndgamePlayer> getPlayerList() {
		return this.playerlist;
	}

	@Override
	public void sendToView(ClientView view) {
		view.show(this);
	}

}
