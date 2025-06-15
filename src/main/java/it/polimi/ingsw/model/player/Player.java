package it.polimi.ingsw.model.player;


import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.board.iPlanche;
import it.polimi.ingsw.model.components.enums.ShipmentType;
import it.polimi.ingsw.model.components.exceptions.AlreadyPoweredException;

import java.io.Serializable;
/**
 * <h2>Player</h2>
 * <p>
 * Represents a player in the game.
 * Each player has a username, color, a spaceship, credits, score,
 * and state flags for retirement and disconnection.
 * </p>
 *
 * <p>The player is responsible for managing their own spaceship,
 * accumulating credits and score, and interacting with the game server
 * through a {@link it.polimi.ingsw.controller.server.ClientDescriptor}.</p>
 */
public class Player implements Serializable {
	private final String username;
	private final PlayerColor color;
	private final SpaceShip ship;
	private transient ClientDescriptor descriptor;
	private int credits;
	private boolean retired = false;
	private boolean disconnected = false;


	public Player(GameModeType gamemode, String username, PlayerColor color) {
		if (username == null || color == PlayerColor.NONE) throw new NullPointerException();
		this.username = username;
		this.color = color;
		ship = new SpaceShip(gamemode, this);
	}

	/**
	 * Returns the username of the player.
	 * @return the player's username
	 */
	public String getUsername() {
		return this.username;
	}

	/**
	 * Returns the color assigned to the player.
	 * @return {@link it.polimi.ingsw.model.player.PlayerColor} The player's color.
	 */
	public PlayerColor getColor() {
		return this.color;
	}

	/**
	 * Marks the player as retired.
	 * Adds all current credits to the score, and half the value of stored cargo.
	 * @throws AlreadyPoweredException if the player is already retired.
	 */
	public void retire() {
		if (this.retired) throw new AlreadyPoweredException("Player: has alredy retired.");
		this.retired = true;
		int sum = 0;
		for (ShipmentType t : ShipmentType.values()) {
			if (t.getValue() <= 0) continue;
			sum += getSpaceShip().getContains()[t.getValue()] * t.getValue();
		}
		this.credits += (sum/2 + sum % 2);
	}

	/**
	 * Checks whether the player has retired.
	 * @return {@code true} if retired, {@code false} otherwise.
	 */
	public boolean getRetired() {
		return this.retired;
	}

	/**
	 * Reconnects the player to the game.
	 * @throws AlreadyPoweredException if the player is already connected.
	 */
	public void reconnect() {
		if (!this.disconnected) throw new AlreadyPoweredException("Player: is already connected.");
		this.disconnected = false;
	}

	/**
	 * Disconnects the player from the game.
	 * @throws AlreadyPoweredException if already disconnected.
	 */
	public void disconnect() {
		if (this.disconnected) throw new AlreadyPoweredException("Player: has already disconnected.");
		this.disconnected = true;
	}

	/**
	 * Checks whether the player is currently disconnected.
	 * @return {@code true} if disconnected.
	 */
	public boolean getDisconnected() {
		return this.disconnected;
	}
	/**
	 * Increases the player's credits by a given amount.
	 * @param amount Credits to be added (must be positive).
	 * @throws IllegalArgumentException if amount is zero or negative.
	 */
	public void giveCredits(int amount) {
		this.credits += amount;
	}

	/**
	 * Returns the current amount of credits.
	 * @return the player's credits
	 */
	public int getCredits() {
		return this.credits;
	}

	/**
	 * Calculates and applies the final score at the end of the game.
	 * Adds score from cargo and remaining credits.
	 */
	public void finalScore() {
		for (ShipmentType t : ShipmentType.values()) {
			if (t.getValue() <= 0) continue;
			this.credits += getSpaceShip().getContains()[t.getValue()] * t.getValue();
		}
	}

	/**
	 * Reconnects the player using a new {@link it.polimi.ingsw.controller.server.ClientDescriptor}.
	 * @param new_descriptor {@link it.polimi.ingsw.controller.server.ClientDescriptor} the new descriptor to bind.
	 */
	public void reconnect(ClientDescriptor new_descriptor) {
		this.bindDescriptor(new_descriptor);
		this.disconnected = false;
	}

	/**
	 * Returns the player's spaceship.
	 * @return {@link it.polimi.ingsw.model.player.SpaceShip} instance.
	 */
	public SpaceShip getSpaceShip() {
		return this.ship;
	}

	/**
	 * Binds a {@link it.polimi.ingsw.controller.server.ClientDescriptor} to the player.
	 * Used during login or reconnection.
	 * @param descriptor {@link it.polimi.ingsw.controller.server.ClientDescriptor} the client descriptor to bind.
	 */
	public void bindDescriptor(ClientDescriptor descriptor) {
		this.descriptor = descriptor;
	}

	/**
	 * Returns the {@link it.polimi.ingsw.controller.server.ClientDescriptor} bound to the player.
	 * @return {@link it.polimi.ingsw.controller.server.ClientDescriptor} Descriptor.
	 */
	public ClientDescriptor getDescriptor() {
		return this.descriptor;
	}

	/**
	 * Returns a formatted string with voyage information:
	 * position, score, credits, engine power, cannon power, crew, battery level.
	 * @param planche the {@link iPlanche} containing position information.
	 * @return string describing the player's current state in the voyage.
	 */
	public String voyageInfo(iPlanche planche) {
		return "[Player: '" + username + "'] Planche position: " + planche.getPlayerPosition(this) + " | Credits: " + credits + " | Engine: " + ship.getEnginePower() + " | Cannon: " + ship.getCannonPower() + " | Crew: " + ship.getTotalCrew() + " | Battery: " + ship.getEnergyPower();
	}

	/**
	 * Compares two players based on their username and color.
	 * @param o the object to compare.
	 * @return {@code true} if same username and color, otherwise {@code false}.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || !(o instanceof Player player)) return false;
		return this.username == player.username
				&& this.color == player.color;
	}
}
