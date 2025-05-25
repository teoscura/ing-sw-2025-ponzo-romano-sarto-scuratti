package it.polimi.ingsw.model.client.state;

import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.client.player.ClientConstructionPlayer;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ClientConstructionState implements ClientState {

	private final GameModeType type;
	private final ArrayList<ClientConstructionPlayer> playerlist;
	private final ArrayList<Integer> construction_cards;
	private final ArrayList<Integer> discarded_tiles;
	private final int tiles_left;
	private final int toggles_total;
	private final int toggles_left;
	private final Duration hourglass_duration;
	private final Instant last_hourglass_toggle;

	public ClientConstructionState(GameModeType type, ArrayList<ClientConstructionPlayer> playerlist, ArrayList<Integer> construction, ArrayList<Integer> discarded, int tiles, int toggles_total, int toggles_left, Duration hourglass_duration, Instant last_hourglass_toggle) {
		if (discarded == null || playerlist == null) throw new NullPointerException();
		if (construction == null && type.getLevel() > 1) throw new IllegalArgumentException();
		if (type.getLevel() > 1) {
			for (int i : construction) {
				if (i < 1 || i > 120 || (i > 20 && i < 100)) throw new IllegalArgumentException();
			}
		}
		for (int i : discarded) {
			if (i < 1 || i > 156) throw new IllegalArgumentException();
		}
		if (tiles < 0) throw new IllegalArgumentException();
		this.type = type;
		this.playerlist = playerlist;
		this.construction_cards = construction;
		this.discarded_tiles = discarded;
		this.tiles_left = tiles;
		this.toggles_total = toggles_total;
		this.toggles_left = toggles_left;
		this.hourglass_duration = hourglass_duration;
		this.last_hourglass_toggle = last_hourglass_toggle;
	}

	public GameModeType getType() {
		return this.type;
	}

	public List<ClientConstructionPlayer> getPlayerList() {
		return this.playerlist;
	}

	public List<Integer> getConstructionCards() {
		return this.construction_cards;
	}

	public List<Integer> getDiscardedTiles() {
		return this.discarded_tiles;
	}

	public int getTilesLeft() {
		return this.tiles_left;
	}

	public int getTogglesTotal() {
		return this.toggles_total;
	}

	public int getTogglesLeft() {
		return this.toggles_left;
	}

	public Duration getHourglassDuration() {
		return this.hourglass_duration;
	}

	public Instant getLastToggle() {
		return this.last_hourglass_toggle;
	}

	@Override
	public void sendToView(ClientStateVisitor visitor) {
		visitor.show(this);
	}

}
