package it.polimi.ingsw.model.player;


import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.board.iPlanche;
import it.polimi.ingsw.model.components.enums.ShipmentType;
import it.polimi.ingsw.model.components.exceptions.AlreadyPoweredException;

import java.io.Serializable;

public class Player implements Serializable {
	private final String username;
	private final PlayerColor color;
	private final SpaceShip ship;
	private transient ClientDescriptor descriptor;
	private int credits;
	private int score;
	private boolean retired = false;
	private boolean disconnected = false;


	public Player(GameModeType gamemode, String username, PlayerColor color) {
		if (username == null || color == PlayerColor.NONE) throw new NullPointerException();
		this.username = username;
		this.color = color;
		ship = new SpaceShip(gamemode, this);
	}

	public String getUsername() {
		return this.username;
	}

	public PlayerColor getColor() {
		return this.color;
	}

	public void retire() {
		if (this.retired) throw new AlreadyPoweredException("Player: has alredy retired.");
		this.retired = true;
		this.score += credits;
		int sum = 0;
		for (ShipmentType t : ShipmentType.values()) {
			if (t.getValue() <= 0) continue;
			sum += getSpaceShip().getContains()[t.getValue()] * t.getValue();
		}
		addScore((sum / 2 + sum % 2));
	}

	public boolean getRetired() {
		return this.retired;
	}

	public void reconnect() {
		if (!this.disconnected) throw new AlreadyPoweredException("Player: is alread y connected.");
		this.disconnected = false;
	}

	public void disconnect() {
		if (this.disconnected) throw new AlreadyPoweredException("Player: has already disconnected.");
		this.disconnected = true;
	}

	public boolean getDisconnected() {
		return this.disconnected;
	}

	public int giveCredits(int amount) {
		this.credits += amount;
		return this.credits;
	}

	public int getCredits() {
		return this.credits;
	}

	public void addScore(int rel_change) {
		this.score += rel_change;
	}

	public int getScore() {
		return this.score;
	}

	public void finalScore() {
		for (ShipmentType t : ShipmentType.values()) {
			if (t.getValue() <= 0) continue;
			this.score += getSpaceShip().getContains()[t.getValue()] * t.getValue();
		}
		this.score += credits;
	}

	public void reconnect(ClientDescriptor new_descriptor) {
		this.bindDescriptor(new_descriptor);
		this.disconnected = false;
	}

	public SpaceShip getSpaceShip() {
		return this.ship;
	}

	public void bindDescriptor(ClientDescriptor descriptor) {
		this.descriptor = descriptor;
	}

	public ClientDescriptor getDescriptor() {
		return this.descriptor;
	}

	public String voyageInfo(iPlanche planche) {
		return "[Player: '" + username + "'] Planche position: " + planche.getPlayerPosition(this) + " | Score: " + score + " | Credits: " + credits + " | Engine: " + ship.getEnginePower() + " | Cannon: " + ship.getCannonPower() + " | Crew: " + ship.getTotalCrew() + " | Battery: " + ship.getEnergyPower();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || !(o instanceof Player player)) return false;
		return this.username == player.username
				&& this.color == player.color;
	}
}