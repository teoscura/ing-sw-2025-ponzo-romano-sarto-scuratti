package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.GameModeType;

import java.io.Serializable;

/**
 * Class representing coordinates on a {@link it.polimi.ingsw.model.player.SpaceShip} of a specified {@link it.polimi.ingsw.model.GameModeType}.
 */
public class ShipCoords implements Serializable {
	public final int x;
	public final int y;
	private final GameModeType type;

	public ShipCoords(GameModeType type, int x, int y) {
		if (x < 0 || y < 0 || x >= type.getWidth() || y >= type.getWidth()) throw new IllegalArgumentException();
		this.type = type;
		this.x = x;
		this.y = y;
	}

	public GameModeType getType() {
		return this.type;
	}

	/**
	 * @return An array of {@link it.polimi.ingsw.model.player.ShipCoords} containing all adjacent coordinates.
	 */
	public ShipCoords[] getNextTo() {
		return new ShipCoords[]{this.up(), this.right(), this.down(), this.left()};
	}

	public ShipCoords up() {
		if (this.y == 0) return new ShipCoords(this.type, 0, 0);
		return new ShipCoords(this.type, this.x, this.y - 1);
	}

	public ShipCoords down() {
		if (this.y == this.type.getHeight() - 1) return new ShipCoords(this.type, 0, 0);
		return new ShipCoords(this.type, this.x, this.y + 1);
	}

	public ShipCoords right() {
		if (this.x == this.type.getWidth() - 1) return new ShipCoords(this.type, 0, 0);
		return new ShipCoords(this.type, this.x + 1, this.y);
	}

	public ShipCoords left() {
		if (this.x == 0) return new ShipCoords(this.type, 0, 0);
		return new ShipCoords(this.type, this.x - 1, this.y);
	}

	public String toString() {
		return "(" + this.x + "," + this.y + ")";
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof ShipCoords c))
			return false;
		return this.x == c.x && this.y == c.y;
	}

}
