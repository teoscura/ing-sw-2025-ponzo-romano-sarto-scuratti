package it.polimi.ingsw.model.client.state;

import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.client.player.ClientWaitingPlayer;
import it.polimi.ingsw.view.ClientView;

import java.util.ArrayList;

public class ClientWaitingRoomState implements ClientState {

	private final GameModeType type;
	private final PlayerCount count;
	private final ArrayList<ClientWaitingPlayer> playerlist;
	

	public ClientWaitingRoomState(GameModeType type, PlayerCount count, ArrayList<ClientWaitingPlayer> playerlist) {
		if (playerlist == null) throw new NullPointerException();
		this.playerlist = playerlist;
		this.type = type;
		this.count = count;
	}

	public GameModeType getType() {
		return this.type;
	}

	public PlayerCount getCount(){
		return this.count;
	}

	public ArrayList<ClientWaitingPlayer> getPlayerList() {
		return this.playerlist;
	}

	@Override
	public void sendToView(ClientView view) {
		view.show(this);
	}

}
