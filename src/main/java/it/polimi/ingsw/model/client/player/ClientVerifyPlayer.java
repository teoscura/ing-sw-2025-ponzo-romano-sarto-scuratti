package it.polimi.ingsw.model.client.player;

import it.polimi.ingsw.model.client.components.ClientSpaceShip;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.VerifyResult;

import java.io.Serializable;

public class ClientVerifyPlayer implements Serializable {

	private final String username;
	private final PlayerColor color;
	private final ClientSpaceShip ship;
	private final VerifyResult[][] results;
	private final boolean finished;
	private final int order;

	public ClientVerifyPlayer(String username, PlayerColor color, ClientSpaceShip ship, VerifyResult[][] results, boolean finished, int order) {
		if (username == null || ship == null || results == null || color == PlayerColor.NONE)
			throw new NullPointerException();
		if (!finished && order > 0) throw new IllegalArgumentException();
		if (results[0].length != ship.getType().getWidth() || results.length != ship.getType().getHeight())
			throw new IllegalArgumentException();
		this.username = username;
		this.color = color;
		this.ship = ship;
		this.results = results;
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

	public VerifyResult[][] getResults() {
		return this.results;
	}

	public boolean isFinished() {
		return finished;
	}

	public int getOrder() {
		return order;
	}

}
