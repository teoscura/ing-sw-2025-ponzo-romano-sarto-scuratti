package it.polimi.ingsw.model.client;

import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.PlayerCount;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * An entry into any list of {@link it.polimi.ingsw.model.ModelInstance} that must be displayed to the player.
 */
public class ClientGameListEntry implements Serializable {

	private final ArrayList<String> players;
	private final int model_id;
	private final GameModeType type;
	private final PlayerCount count;
	private final String state;

	public ClientGameListEntry(GameModeType type, PlayerCount count, String state, List<String> players, int model_id) {
		if (players == null || type == null || state == null) throw new NullPointerException();
		if (players.size() < 0 || players.size() > 4 || model_id < 0) throw new IllegalArgumentException();
		this.players = new ArrayList<>(players);
		this.model_id = model_id;
		this.type = type;
		this.count = count;
		this.state = state;
	}

	public GameModeType getType() {
		return this.type;
	}

	public PlayerCount getCount() {
		return this.count;
	}

	public ArrayList<String> getPlayers() {
		return this.players;
	}

	public int getModelId() {
		return this.model_id;
	}

	public String getState() {
		return this.state;
	}

}

