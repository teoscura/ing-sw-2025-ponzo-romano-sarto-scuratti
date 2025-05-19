//Done.
package it.polimi.ingsw.model.components;

import it.polimi.ingsw.model.client.components.ClientBaseComponent;
import it.polimi.ingsw.model.client.components.ClientComponent;
import it.polimi.ingsw.model.client.components.ClientShipmentsComponentDecorator;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import it.polimi.ingsw.model.components.enums.ShipmentType;
import it.polimi.ingsw.model.components.enums.StorageType;
import it.polimi.ingsw.model.components.exceptions.ContainerEmptyException;
import it.polimi.ingsw.model.components.exceptions.ContainerFullException;
import it.polimi.ingsw.model.components.exceptions.ContainerNotSpecialException;
import it.polimi.ingsw.model.components.visitors.iVisitor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.player.SpaceShip;

import java.util.Arrays;

/**
 * <h2>StorageComponent</h2>
 * <p>
 * Represents a cargo storage module on a player's spaceship in <i>Galaxy Trucker</i>.
 * It can contain shipments of various types, such as goods or special items (e.g. red containers).
 * </p>
 */
public class StorageComponent extends BaseComponent {

	private final int[] shipments;
	private final StorageType type;
	private int currently_full = 0;

	public StorageComponent(int id,
							ConnectorType[] connectors,
							ComponentRotation rotation,
							StorageType type) {
		super(id, connectors, rotation);
		this.type = type;
		this.shipments = new int[4];
		Arrays.fill(shipments, 0);
	}

	public StorageComponent(int id,
							ConnectorType[] connectors,
							ComponentRotation rotation,
							StorageType type,
							ShipCoords coords) {
		super(id, connectors, rotation, coords);
		this.type = type;
		this.shipments = new int[4];
		Arrays.fill(shipments, 0);
	}

	/**
	 * Receives a shipment and adds it to the storage
	 *
	 * @throws ContainerFullException if the component is already full
	 *
	 * @param shipment
	 */
	public void putIn(ShipmentType shipment) {
		if (shipment.getValue() < 1) throw new IllegalArgumentException();
		if (shipment.getSpecial() && !this.type.getSpecial()) throw new ContainerNotSpecialException();
		if (this.currently_full == this.type.getCapacity()) throw new ContainerFullException();
		this.shipments[shipment.getValue() - 1]++;
		this.currently_full++;
	}

	/**
	 * Removes shipment of a specific type from the storage
	 *
	 * @param shipment
	 * @return true if shipment is present and removed, false otherwise
	 */
	public boolean takeOut(ShipmentType shipment) {
		if (shipment.getValue() < 1) throw new IllegalArgumentException();
		if (shipment.getSpecial() && !this.type.getSpecial()) return false;
		if (this.currently_full == 0) throw new ContainerEmptyException();
		if (this.shipments[shipment.getValue() - 1] <= 0) throw new ContainerEmptyException();
		this.shipments[shipment.getValue() - 1]--;
		this.currently_full--;
		return true;
	}

	/**
	 * @param container
	 * @return how many shipments of the specified container type are in the storage
	 */
	public int howMany(ShipmentType container) {
		return this.shipments[container.getValue() - 1];
	}

	public int getFreeSpaces() {
		return this.type.getCapacity() - this.currently_full;
	}

	public boolean getSpecial() {
		return this.type.getSpecial();
	}

	public int getCapacity() {
		return this.type.getCapacity();
	}

	/**
	 * This adds the Storage Component's coordinates to the {@link SpaceShip}
	 * @param ship  {@link SpaceShip} to which you want to add the Storage component
	 */
	@Override
	public void onCreation(SpaceShip ship, ShipCoords coords) {
		this.coords = coords;
		ship.addStorageCoords(this.coords);
	}

	/**
	 * This removes the Storage Component's coordinates from the {@link SpaceShip}
	 * @param ship  {@link SpaceShip} to which you want to remove the Storage component
	 */
	@Override
	public void onDelete(SpaceShip ship) {
		ship.delStorageCoords(this.coords);
	}

	@Override
	public void check(iVisitor v) {
		v.visit(this);
	}

	@Override
	public ClientComponent getClientComponent() {
		return new ClientShipmentsComponentDecorator(new ClientBaseComponent(getID(), getRotation(), getConnectors()), type, shipments);
	}

}