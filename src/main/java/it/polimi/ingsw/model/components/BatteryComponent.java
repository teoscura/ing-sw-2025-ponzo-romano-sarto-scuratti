//Done.
package it.polimi.ingsw.model.components;

import it.polimi.ingsw.model.client.components.ClientBaseComponent;
import it.polimi.ingsw.model.client.components.ClientBatteryComponentDecorator;
import it.polimi.ingsw.model.client.components.ClientComponent;
import it.polimi.ingsw.model.components.enums.BatteryType;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import it.polimi.ingsw.model.components.exceptions.ContainerEmptyException;
import it.polimi.ingsw.model.components.exceptions.ContainerFullException;
import it.polimi.ingsw.model.components.visitors.iVisitor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.player.SpaceShip;
/**
 * <h2>BatteryComponent</h2>
 * <p>
 * This class models a ship component that contains a rechargeable battery.
 * It is used to store and distribute energy to components like <i>double engines</i>, <i>cannons</i>,
 * and other modules that require energy to activate.
 * </p>
 */
public class BatteryComponent extends BaseComponent {

	private final int max;
	private int contains;

	public BatteryComponent(int id,
							ConnectorType[] connectors,
							ComponentRotation rotation,
							BatteryType type) {
		super(id, connectors, rotation);
		this.contains = type.getCapacity();
		this.max = type.getCapacity();
	}

	public BatteryComponent(int id,
							ConnectorType[] connectors,
							ComponentRotation rotation,
							BatteryType type,
							ShipCoords coords) {
		super(id, connectors, rotation, coords);
		this.contains = type.getCapacity();
		this.max = type.getCapacity();
	}

	@Override
	public void check(iVisitor v) {
		v.visit(this);
	}

	/**
	 * Returns the current batteries contained in the battery component.
	 * @return the current batteries contained
	 */
	public int getContains() {
		return this.contains;
	}

	/**
	 * Returns the maximum capacity of the battery component.
	 * @return maximum capacity
	 */
	public int getCapacity() {
		return this.max;
	}

	/**
	 * Takes one battery from the battery component.
	 * @throws ContainerEmptyException if the component is already empty.
	 */
	public void takeOne() {
		if (contains == 0) {
			throw new ContainerEmptyException();
		}
		this.contains--;
	}

	/**
	 * Adds one battery to the battery component.
	 * @throws ContainerFullException if capacity is already max.
	 */
	public void putOne() {
		if (contains == max) {
			throw new ContainerFullException();
		}
		contains++;
	}

	/**
	 * This adds the Battery Component's coordinates to the {@link SpaceShip}
	 * @param ship  {@link SpaceShip} to which you want to add the battery component
	 */
	@Override
	public void onCreation(SpaceShip ship, ShipCoords coords) {
		this.coords = coords;
		ship.addBatteryCoords(this.coords);
	}

	/**
	 * This removes the Battery Component's coordinates from the {@link SpaceShip}
	 * @param ship  {@link SpaceShip} to which you want to remove the battery component
	 */
	@Override
	public void onDelete(SpaceShip ship) {
		ship.delBatteryCoords(this.coords);
	}

	@Override
	public ClientComponent getClientComponent() {
		return new ClientBatteryComponentDecorator(new ClientBaseComponent(this.getID(), getRotation(), getConnectors()), this.contains);
	}
}


