package it.polimi.ingsw.model.client.state;

import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.client.card.ClientCardState;
import it.polimi.ingsw.model.client.player.ClientVoyagePlayer;
import it.polimi.ingsw.view.ClientView;

import java.util.ArrayList;

public class ClientVoyageState implements ClientState {

	private final GameModeType type;
	private final ArrayList<ClientVoyagePlayer> playerlist;
	private final ClientCardState card_state;

	public ClientVoyageState(GameModeType type, ArrayList<ClientVoyagePlayer> playerlist, ClientCardState card_state) {
		if (type == null || playerlist == null) throw new NullPointerException();
		this.type = type;
		this.playerlist = playerlist;;
		this.card_state = card_state;
	}

	public GameModeType getType() {
		return this.type;
	}

	public ArrayList<ClientVoyagePlayer> getPlayerList() {
		return this.playerlist;
	}

	public ClientCardState getCardState() {
		return this.card_state;
	}

	@Override
	public void sendToView(ClientView view) {
		view.show(this);
	}

}
