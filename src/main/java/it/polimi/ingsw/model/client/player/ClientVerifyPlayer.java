package it.polimi.ingsw.model.client.player;

import it.polimi.ingsw.model.client.components.ClientSpaceShip;
import it.polimi.ingsw.model.player.PlayerColor;

import java.io.Serializable;

public class ClientVerifyPlayer implements Serializable {

	private final String username;
	private final PlayerColor color;
	private final ClientSpaceShip ship;
	private final boolean finished;
	private final int order;

	public ClientVerifyPlayer(String username, PlayerColor color, ClientSpaceShip ship, boolean finished, int order) {
		if (username == null || ship == null || color == PlayerColor.NONE)
			throw new NullPointerException();
		if (!finished && order > 0) throw new IllegalArgumentException();
		this.username = username;
		this.color = color;
		this.ship = ship;
		this.finished = finished;
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

	public boolean isFinished() {
		return finished;
	}

	public int getOrder() {
		return order;
	}

}
