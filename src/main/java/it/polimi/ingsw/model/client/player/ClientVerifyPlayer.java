package it.polimi.ingsw.model.client.player;

import it.polimi.ingsw.model.client.components.ClientSpaceShip;
import it.polimi.ingsw.model.player.PlayerColor;

import java.io.Serializable;

public class ClientVerifyPlayer implements Serializable {

	private final String username;
	private final PlayerColor color;
	private final ClientSpaceShip ship;
	private final boolean valid;
	private final boolean progressed;
	private final boolean starts_losing;
	private final int order;

	public ClientVerifyPlayer(String username, PlayerColor color, ClientSpaceShip ship, boolean valid, boolean progressed, boolean starts_losing, int order) {
		if (username == null || ship == null || color == PlayerColor.NONE)
			throw new NullPointerException();
		if (!valid && order > 0) throw new IllegalArgumentException();
		this.username = username;
		this.color = color;
		this.ship = ship;
		this.valid = valid;
		this.progressed = progressed;
		this.starts_losing = starts_losing;
		this.order = order;
	}

	public String getUsername() {
		return username;
	}

	public PlayerColor getColor() {
		return color;
	}

	public ClientSpaceShip getShip() {
		return this.ship;
	}

	public boolean isValid() {
		return this.valid;
	}

	public boolean hasProgressed(){
		return this.progressed;
	}

	public boolean startsLosing(){
		return this.starts_losing;
	}

	public int getOrder() {
		return order;
	}

}
