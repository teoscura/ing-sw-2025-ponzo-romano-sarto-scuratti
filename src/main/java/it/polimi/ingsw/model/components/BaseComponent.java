//Done.
package it.polimi.ingsw.model.components;

import it.polimi.ingsw.model.client.components.ClientComponent;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import it.polimi.ingsw.model.components.exceptions.ConnectorsSizeException;
import it.polimi.ingsw.model.components.visitors.ComponentVisitor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.player.SpaceShip;

import java.io.Serializable;

/**
 * Abstract class representing a server side component tile.
 */
public abstract class BaseComponent implements Serializable {

	private final int id;
	private final ConnectorType[] connectors;
	protected ShipCoords coords;
	private ComponentRotation rotation;

	protected BaseComponent(int id, ConnectorType[] connectors,
							ComponentRotation rotation) {
		if (connectors.length != 4) {
			throw new ConnectorsSizeException();
		}
		if (id < 1 || id > 157) throw new IllegalArgumentException("Id is not valid.");
		this.id = id;
		this.connectors = connectors;
		this.rotation = rotation;
	}


	protected BaseComponent(int id, ConnectorType[] connectors,
							ComponentRotation rotation,
							ShipCoords coords) {
		if (connectors.length != 4) {
			throw new ConnectorsSizeException();
		}
		if (id < 1 || id > 157) throw new IllegalArgumentException("Id is not valid.");
		this.id = id;
		this.connectors = connectors;
		this.rotation = rotation;
		this.coords = coords;
	}


	public int getID() {
		return this.id;
	}

	/**
	 * @return Array of {@link ConnectorType}, one for each cardinal direction starting clockwise from the top.
	 */
	public ConnectorType[] getConnectors() {
		return connectors;
	}

	/**
	 * @return Rotation of the component, given as the clockwise angle from the up position
	 */
	public ComponentRotation getRotation() {
		return rotation;
	}

	/**
	 * Rotates the component.
	 * 
	 * @param rotation {@link ComponentRotation} New component rotation.
	 */
	public void rotate(ComponentRotation rotation) {
		this.rotation = rotation;
	}


	/**
	 * Verifies the component is properly connected to its neighbours.
	 * 
	 * @param ship {@link SpaceShip} Ship from which the component retrieves its neighbours.
	 * @return Whether the component is properly connected or not.
	 */
	public boolean verify(SpaceShip ship) {
		if (this.coords == null) throw new NullPointerException("Coords are not set");
		BaseComponent up = ship.getComponent(this.coords.up());
		BaseComponent right = ship.getComponent(this.coords.right());
		BaseComponent down = ship.getComponent(this.coords.down());
		BaseComponent left = ship.getComponent(this.coords.left());
		if (up != ship.getEmpty()) {
			if (!up.getConnector(ComponentRotation.U180).compatible(getConnector(ComponentRotation.U000))) return false;
		}
		if (right != ship.getEmpty()) {
			if (!right.getConnector(ComponentRotation.U270).compatible(getConnector(ComponentRotation.U090)))
				return false;
		}
		if (down != ship.getEmpty()) {
			if (!down.getConnector(ComponentRotation.U000).compatible(getConnector(ComponentRotation.U180)))
				return false;
		}
		if (left != ship.getEmpty()) {
			return left.getConnector(ComponentRotation.U090).compatible(getConnector(ComponentRotation.U270));
		}
		return true;
	}


	/**
	 * Returns the connector at a specific direction of the component, given the rotation of the component inside the ship
	 *
	 * @param direction {@link ComponentRotation} Rotation requested.
	 * @return {@link ConnectorType} Connector at that specific rotation.
	 */
	public ConnectorType getConnector(ComponentRotation direction) {
		int shift = direction.getShift() + (4 - this.rotation.getShift());
		shift = shift % 4;
		return connectors[shift];
	}

	/**
	 * @return coords of the component inside the ship
	 */
	public ShipCoords getCoords() {
		return this.coords;
	}

	/**
	 * @return Whether the component can be powered or not.
	 */
	public boolean powerable() {
		return false;
	}

	/**
	 * Logic when component is placed in {@link SpaceShip ship}.
	 * @param ship {@link SpaceShip} Ship where the component was placed.
	 * @param coords {@link ShipCoords} Coordinates where it was placed.
	 */
	public abstract void onCreation(SpaceShip ship, ShipCoords coords);

	/**
	 * Logic when component is removed from {@link SpaceShip ship}.
	 * @param ship {@link SpaceShip} Ship where the component was placed.
	 */
	public abstract void onDelete(SpaceShip ship);

	/**
	 * @return {@link ClientComponent }
	 */
	public abstract ClientComponent getClientComponent();

	/**
	 * Checks the component using the visitor provided.
	 *
	 * @param v {@link ComponentVisitor} Visitor to show component to.
	 */
	public abstract void check(ComponentVisitor v);

	/**
	 * Checks if adjacent components have compatible connectors, if true adds component to an array
	 *
	 * @param ship {@link SpaceShip} Ship to retrieve connected components.
	 * @return Array of {@link BaseComponent} guaranteed to be connected to this.
	 */
	public BaseComponent[] getConnectedComponents(SpaceShip ship) {
		BaseComponent[] res = new BaseComponent[]{ship.getEmpty(), ship.getEmpty(), ship.getEmpty(), ship.getEmpty()};
		if (ship.getComponent(this.getCoords().up()) != ship.getEmpty()) {
			if (this.getConnector(ComponentRotation.U000)
					.connected(ship.getComponent(this.getCoords().up())
							.getConnector(ComponentRotation.U180))) {
				res[0] = ship.getComponent(this.getCoords().up());
			}
		}
		if (ship.getComponent(this.getCoords().right()) != ship.getEmpty()) {
			if (this.getConnector(ComponentRotation.U090)
					.connected(ship.getComponent(this.getCoords().right())
							.getConnector(ComponentRotation.U270))) {
				res[1] = ship.getComponent(this.getCoords().right());
			}
		}
		if (ship.getComponent(this.getCoords().down()) != ship.getEmpty()) {
			if (this.getConnector(ComponentRotation.U180)
					.connected(ship.getComponent(this.getCoords().down())
							.getConnector(ComponentRotation.U000))) {
				res[2] = ship.getComponent(this.getCoords().down());
			}
		}
		if (ship.getComponent(this.getCoords().left()) != ship.getEmpty()) {
			if (this.getConnector(ComponentRotation.U270)
					.connected(ship.getComponent(this.getCoords().left())
							.getConnector(ComponentRotation.U090))) {
				res[3] = ship.getComponent(this.getCoords().left());
			}
		}
		return res;
	}

}
