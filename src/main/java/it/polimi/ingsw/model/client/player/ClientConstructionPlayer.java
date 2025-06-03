package it.polimi.ingsw.model.client.player;

import it.polimi.ingsw.model.client.components.ClientSpaceShip;
import it.polimi.ingsw.model.player.PlayerColor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ClientConstructionPlayer implements Serializable {

	private final String username;
	private final PlayerColor color;
	private final ClientSpaceShip ship;
	private final int current_component;
	private final ArrayList<Integer> reserved_components;
	private final boolean finished;
	private final boolean disconnected;

	public ClientConstructionPlayer(String username, PlayerColor color, ClientSpaceShip ship, int current_component,
									ArrayList<Integer> reserved_components, boolean finished, boolean disconnected) {
		if (username == null || ship == null || reserved_components == null || color == PlayerColor.NONE)
			throw new NullPointerException();
		if (reserved_components.size() > 2) throw new IllegalArgumentException();
		this.username = username;
		this.color = color;
		this.ship = ship;
		this.current_component = current_component;
		this.reserved_components = reserved_components;
		this.finished = finished;
		this.disconnected = disconnected;
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

	public int getCurrent(){
		return this.current_component;
	}

	public List<Integer> getReserved() {
		return this.reserved_components;
	}

	public boolean isFinished() {
		return this.finished;
	}

	public boolean isDisconnected(){
		return this.disconnected;
	}

}
