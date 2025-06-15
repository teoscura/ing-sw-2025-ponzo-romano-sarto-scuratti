package it.polimi.ingsw.model.client.player;

import it.polimi.ingsw.model.player.PlayerColor;

import java.io.Serializable;

/**
 * Client side representation of a player during a {@link WaitingState}
 */
public class ClientWaitingPlayer implements Serializable {

	private final String username;
	private final PlayerColor color;

	public ClientWaitingPlayer(String username, PlayerColor color) {
		if (username == null) throw new NullPointerException();
		this.username = username;
		this.color = color;
	}

	public String getUsername() {
		return this.username;
	}

	public PlayerColor getColor() {
		return this.color;
	}
}
