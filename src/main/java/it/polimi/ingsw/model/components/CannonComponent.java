//Done.
package it.polimi.ingsw.model.components;

import it.polimi.ingsw.model.client.components.ClientBaseComponent;
import it.polimi.ingsw.model.client.components.ClientCannonComponentDecorator;
import it.polimi.ingsw.model.client.components.ClientComponent;
import it.polimi.ingsw.model.client.components.ClientPoweredComponentDecorator;
import it.polimi.ingsw.model.components.enums.CannonType;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import it.polimi.ingsw.model.components.exceptions.AlreadyPoweredException;
import it.polimi.ingsw.model.components.exceptions.ComponentNotEmptyException;
import it.polimi.ingsw.model.components.exceptions.UnpowerableException;
import it.polimi.ingsw.model.components.visitors.iVisitor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.player.SpaceShip;

/**
 * <h2>CannonComponent</h2>
 * <p>
 * This class represents a <strong>cannon</strong> component that can be attached to a {@link SpaceShip}.
 * Cannons are used to shoot at enemies during specific adventure cards or game phases.
 * Depending on their type, cannons may be powerable (require battery activation) or not.
 * </p>
 */
public class CannonComponent extends BaseComponent {

	private final double max_power;
	private final boolean powerable;
	private boolean powered = false;

	public CannonComponent(int id,
						   ConnectorType[] components,
						   ComponentRotation rotation,
						   CannonType type) {
		super(id, components, rotation);
		if (components[0] != ConnectorType.EMPTY) throw new ComponentNotEmptyException("Top of cannon must be empty!");
		this.max_power = type.getMaxPower();
		this.powerable = type.getPowerable();
	}

	public CannonComponent(int id,
						   ConnectorType[] components,
						   ComponentRotation rotation,
						   CannonType type,
						   ShipCoords coords) {
		super(id, components, rotation, coords);
		if (components[0] != ConnectorType.EMPTY) throw new ComponentNotEmptyException("Top of cannon must be empty!");
		this.max_power = type.getMaxPower();
		this.powerable = type.getPowerable();
	}

	@Override
	public void check(iVisitor v) {
		v.visit(this);
	}

	/**Checks whether the cannon can be placed in the current position on the ship.
	 *@param ship {@link SpaceShip} to which you want to add the cannon component
	 */
	@Override
	public boolean verify(SpaceShip ship) {
		ComponentRotation r = this.getRotation();
		BaseComponent tmp = null;
		switch (r.getShift()) {
			case 0: {
				tmp = ship.getComponent(this.coords.up());
				break;
			}
			case 1: {
				tmp = ship.getComponent(this.coords.right());
				break;
			}
			case 2: {
				tmp = ship.getComponent(this.coords.down());
				break;
			}
			case 3: {
				tmp = ship.getComponent(this.coords.left());
			}
		}
		return tmp == ship.getEmpty() && super.verify(ship);
	}

	/**
	 * turn on the cannon component
	 *
	 * @throws AlreadyPoweredException if the cannon is already powered.
	 * @throws UnpowerableException if the cannon is unpowerable.
	 */
	public void turnOn() {
		if (this.powered) throw new AlreadyPoweredException();
		if (!this.powerable) throw new UnpowerableException();
		this.powered = true;
	}

	public void turnOff() {
		this.powered = false;
	}

	public double getCurrentPower() {
		if (this.getRotation() != ComponentRotation.U000) {
			return this.getPower() / 2;
		}
		return this.getPower();
	}

	private double getPower() {
		if (this.powerable) return this.powered ? max_power : 0;
		return this.max_power;
	}

	@Override
	public boolean powerable() {
		return this.powerable;
	}

	/**
	 * This adds the Cannon Component's coordinates to the {@link SpaceShip}
	 * @param ship  {@link SpaceShip} to which you want to add the Cannon component
	 */
	@Override
	public void onCreation(SpaceShip ship, ShipCoords coords) {
		this.coords = coords;
		if (this.powerable) ship.addPowerableCoords(coords);
	}

	/**
	 * This removes the Cannon Component's coordinates from the {@link SpaceShip}
	 * @param ship  {@link SpaceShip} to which you want to remove the Cannon component
	 */
	@Override
	public void onDelete(SpaceShip ship) {
		if (powerable) ship.delPowerableCoords(coords);
	}

	@Override
	public ClientComponent getClientComponent() {
		ClientComponent c = new ClientCannonComponentDecorator(new ClientBaseComponent(getID(), getRotation(), getConnectors()), getRotation());
		if (!powerable) return c;
		return new ClientPoweredComponentDecorator(c, powered);
	}

}
