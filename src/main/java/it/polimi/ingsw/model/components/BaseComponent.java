//Done.
package it.polimi.ingsw.model.components;

import it.polimi.ingsw.model.client.components.ClientComponent;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import it.polimi.ingsw.model.components.exceptions.ConnectorsSizeException;
import it.polimi.ingsw.model.components.visitors.iVisitable;
import it.polimi.ingsw.model.components.visitors.iVisitor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.player.SpaceShip;

import java.io.Serializable;

public abstract class BaseComponent implements iVisitable, Serializable {

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


	/**
	 * Returns the unique id of the card
	 *
	 * @return id
	 */
	public int getID() {
		return this.id;
	}

	/**
	 * Return an array of 4 elements, representing the connectors clockwise starting from above the component
	 *
	 * @return connectors
	 */
	public ConnectorType[] getConnectors() {
		return connectors;
	}


	/**
	 * Returns the rotation of the component, given as the clockwise angle from the up position
	 *
	 * @return rotation
	 */
	public ComponentRotation getRotation() {
		return rotation;
	}


	public void rotate(ComponentRotation rotation) {
		this.rotation = rotation;
	}


	/**
	 * Checks each adjacent component in the 4 directions, verifies if the connectors on the adjacent sides are compatible with each other
	 *
	 * @param ship
	 * @return true if no errors are found, false if at least one component is not connected correctly
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
	 * @param direction
	 * @return connector
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
	 * @return if component is powerable, false unless specified otherwise
	 */

	public boolean powerable() {
		return false;
	}


	public abstract void onCreation(SpaceShip ship, ShipCoords coords);


	public abstract void onDelete(SpaceShip ship);


	public abstract ClientComponent getClientComponent();

	/**
	 * Calls the visitor on the component, action depends on the component found
	 *
	 * @param v
	 */
	public abstract void check(iVisitor v);

	/**
	 * Checks if adjacent components have compatible connectors, if true adds component to an array
	 *
	 * @param ship
	 * @return res: array of components
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
