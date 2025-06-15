package it.polimi.ingsw.model.client.state;

import it.polimi.ingsw.model.client.player.ClientEndgamePlayer;
import it.polimi.ingsw.model.player.PlayerColor;

import java.util.ArrayList;
import java.util.List;

/**
 * Client side representation of a {@link it.polimi.ingsw.model.state.EndscreenState}.
 */
public class ClientEndgameState implements ClientState {

	private final ArrayList<ClientEndgamePlayer> playerlist;
	private final ArrayList<PlayerColor> awaiting;

	public ClientEndgameState(ArrayList<ClientEndgamePlayer> playerlist, List<PlayerColor> awaiting) {
		this.playerlist = playerlist;
		this.awaiting = new ArrayList<>(awaiting);
	}

	public ArrayList<ClientEndgamePlayer> getPlayerList() {
		return this.playerlist;
	}

	public ArrayList<PlayerColor> awaiting(){
		return this.awaiting;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendToView(ClientStateVisitor visitor) {
		visitor.show(this);
	}

}
