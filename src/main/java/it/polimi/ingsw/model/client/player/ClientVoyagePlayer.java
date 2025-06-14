package it.polimi.ingsw.model.client.player;

import it.polimi.ingsw.model.client.components.ClientSpaceShip;
import it.polimi.ingsw.model.player.PlayerColor;

import java.io.Serializable;

/**
 * Client side representation of a player during a {@link VoyageState}
 */
public class ClientVoyagePlayer implements Serializable {

	private final String username;
	private final PlayerColor color;
	private final ClientSpaceShip ship;
	private final int planche_slot;
	private final int credits;
	private final boolean disconnected;
	private final boolean retired_lost;

	public ClientVoyagePlayer(String username, PlayerColor color, ClientSpaceShip ship, int planche_slot,
							  int credits, boolean disconnected, boolean retired_lost) {
		if (username == null || ship == null) throw new NullPointerException();
		if (planche_slot < -1) throw new IllegalArgumentException();
		this.username = username;
		this.color = color;
		this.ship = ship;
		this.planche_slot = planche_slot;
		this.credits = credits;
		this.disconnected = disconnected;
		this.retired_lost = retired_lost;
	}

	public String getUsername() {
		return this.username;
	}

	public PlayerColor getColor() {
		return this.color;
	}

	public ClientSpaceShip getShip() {
		return this.ship;
	}

	public int getPlancheSlot() {
		return this.planche_slot;
	}

	public int getCredits() {
		return this.credits;
	}

	public boolean isDisconnected() {
		return this.disconnected;
	}

	public boolean isRetired() {
		return this.retired_lost;
	}

}
