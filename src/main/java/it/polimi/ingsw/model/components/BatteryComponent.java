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

	public int getContains() {
		return this.contains;
	}

	public int getCapacity() {
		return this.max;
	}

	public void takeOne() {
		if (contains == 0) {
			throw new ContainerEmptyException();
		}
		this.contains--;
	}

	public void putOne() {
		if (contains == max) {
			throw new ContainerFullException();
		}
		contains++;
	}

	@Override
	public void onCreation(SpaceShip ship, ShipCoords coords) {
		this.coords = coords;
		ship.addBatteryCoords(this.coords);
	}

	@Override
	public void onDelete(SpaceShip ship) {
		ship.delBatteryCoords(this.coords);
	}

	@Override
	public ClientComponent getClientComponent() {
		return new ClientBatteryComponentDecorator(new ClientBaseComponent(this.getID(), getRotation()), this.contains);
	}
}


