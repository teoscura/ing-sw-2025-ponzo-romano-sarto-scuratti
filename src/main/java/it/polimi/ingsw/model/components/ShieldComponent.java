//Done.
package it.polimi.ingsw.model.components;

import it.polimi.ingsw.model.client.components.ClientBaseComponent;
import it.polimi.ingsw.model.client.components.ClientComponent;
import it.polimi.ingsw.model.client.components.ClientPoweredComponentDecorator;
import it.polimi.ingsw.model.client.components.ClientShieldComponentDecorator;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import it.polimi.ingsw.model.components.enums.ShieldType;
import it.polimi.ingsw.model.components.exceptions.AlreadyPoweredException;
import it.polimi.ingsw.model.components.visitors.iVisitor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.player.SpaceShip;

/**
 * <h2>ShieldComponent</h2>
 * <p>
 * Represents a <strong>shield generator</strong> component in a spaceship. Shields are used to defend the ship
 * from incoming projectiles, protecting a directional arc of the ship depending on the component's rotation.
 * </p>
 */
public class ShieldComponent extends BaseComponent {

	private boolean powered = false;

	public ShieldComponent(int id, ConnectorType[] connectors,
						   ComponentRotation rotation) {
		super(id, connectors, rotation);
	}

	public ShieldComponent(int id, ConnectorType[] connectors,
						   ComponentRotation rotation,
						   ShipCoords coords) {
		super(id, connectors, rotation, coords);
	}

	@Override
	public void check(iVisitor v) {
		v.visit(this);
	}

	/**
	 * turn on the shield component
	 *
	 * @throws AlreadyPoweredException if the shield is already powered.
	 */
	public void turnOn() {
		if (this.powered) throw new AlreadyPoweredException();
		this.powered = true;
	}

	public void turnOff() {
		this.powered = false;
	}

	public boolean getPowered() {
		return this.powered;
	}

	@Override
	public boolean powerable() {
		return true;
	}

	/**
	 * This adds the Shield Component's coordinates to the {@link SpaceShip}
	 * @param ship  {@link SpaceShip} to which you want to add the shield component
	 */
	@Override
	public void onCreation(SpaceShip ship, ShipCoords coords) {
		this.coords = coords;
		ship.addPowerableCoords(this.coords);
	}

	/**
	 * This removes the Shield Component's coordinates from the {@link SpaceShip}
	 * @param ship  {@link SpaceShip} to which you want to remove the shield component
	 */
	@Override
	public void onDelete(SpaceShip ship) {
		ship.delPowerableCoords(this.coords);
	}

	/**
	 * Returns the shield type based on rotation
	 * if the shield is on. If off, returns {@link ShieldType#NONE}.
	 */
	public ShieldType getShield() {
		if (!this.powered) return ShieldType.NONE;
		return ShieldType.values()[this.getRotation().getShift()];
	}

	@Override
	public ClientComponent getClientComponent() {
		return new ClientPoweredComponentDecorator(
				new ClientShieldComponentDecorator(
						new ClientBaseComponent(getID(), getRotation(), getConnectors()), ShieldType.values()[this.getRotation().getShift()]),
				powered);
	}

}
