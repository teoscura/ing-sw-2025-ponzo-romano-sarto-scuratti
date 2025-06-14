package it.polimi.ingsw.model.client.player;

import it.polimi.ingsw.model.player.PlayerColor;

import java.io.Serializable;

/**
 * Client side representation of a player during a {@link EndscreenState}
 */
public class ClientEndgamePlayer implements Serializable {

	private final String username;
	private final PlayerColor color;
	private final int planche_slot;
	private final int credits;
	private final int[] shipments;

	public ClientEndgamePlayer(String username, PlayerColor color, int planche_slot, int credits, int[] shipments) {
		if (username == null || shipments == null || color == PlayerColor.NONE) throw new NullPointerException();
		if (shipments.length != 5) throw new IllegalArgumentException();
		this.username = username;
		this.color = color;
		this.planche_slot = planche_slot;
		this.credits = credits;
		this.shipments = shipments;
	}

	public String getUsername() {
		return this.username;
	}

	public PlayerColor getColor() {
		return this.color;
	}

	public int getPlanche_slot() {
		return this.planche_slot;
	}

	public int getCredits() {
		return this.credits;
	}

	public int[] getShipments() {
		return this.shipments;
	}

}
