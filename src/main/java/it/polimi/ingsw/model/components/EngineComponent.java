//Done.
package it.polimi.ingsw.model.components;

import it.polimi.ingsw.model.client.components.ClientBaseComponent;
import it.polimi.ingsw.model.client.components.ClientComponent;
import it.polimi.ingsw.model.client.components.ClientEngineComponentDecorator;
import it.polimi.ingsw.model.client.components.ClientPoweredComponentDecorator;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import it.polimi.ingsw.model.components.enums.EngineType;
import it.polimi.ingsw.model.components.exceptions.AlreadyPoweredException;
import it.polimi.ingsw.model.components.exceptions.ComponentNotEmptyException;
import it.polimi.ingsw.model.components.exceptions.UnpowerableException;
import it.polimi.ingsw.model.components.visitors.ComponentVisitor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.player.SpaceShip;

/**
 * <h2>EngineComponent</h2>
 * <p>
 * This class represents an <strong>engine</strong> component in a {@link SpaceShip}.
 * Engines provide propulsion and allow the ship to move forward on the game board.
 * They can be of different types: either always active (non-powerable) or requiring battery power (powerable).
 * </p>
 */
public class EngineComponent extends BaseComponent {

	private final int max_power;
	private final boolean powerable;
	private boolean powered = false;

	public EngineComponent(int id,
						   ConnectorType[] components,
						   ComponentRotation rotation,
						   EngineType type) {
		super(id, components, rotation);
		if (components[2] != ConnectorType.EMPTY)
			throw new ComponentNotEmptyException("Bottom of engine must be empty!");
		this.max_power = type.getMaxPower();
		this.powerable = type.getPowerable();
	}

	public EngineComponent(int id,
						   ConnectorType[] components,
						   ComponentRotation rotation,
						   EngineType type,
						   ShipCoords coords) {
		super(id, components, rotation, coords);
		if (components[2] != ConnectorType.EMPTY)
			throw new ComponentNotEmptyException("Bottom of engine must be empty!");
		this.max_power = type.getMaxPower();
		this.powerable = type.getPowerable();
	}

	/**Verify that there isn't a component under the Engine Component
	 * @param ship {@link SpaceShip} Ship to retrieve the component under it.
	 * @return {@code true} if the cell below is empty, {@code false} otherwise
	 */
	@Override
	public boolean verify(SpaceShip ship) {
		if (this.getRotation() != ComponentRotation.U000 || ship.getComponent(this.coords.down()) != ship.getEmpty())
			return false;
		return super.verify(ship);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void check(ComponentVisitor v) {
		v.visit(this);
	}

	/**
	 * Turn on the engine component.
	 *
	 * @throws AlreadyPoweredException if the engine is already powered
	 * @throws UnpowerableException if the engine is unpowerable
	 */
	public void turnOn() {
		if (this.powered) throw new AlreadyPoweredException();
		if (!this.powerable) throw new UnpowerableException();
		this.powered = true;
	}

	public void turnOff() {
		this.powered = false;
	}

	public int getCurrentPower() {
		if (this.getRotation() != ComponentRotation.U000) {
			return 0;
		}
		return this.getPower();
	}

	private int getPower() {
		if (this.powerable) return this.powered ? max_power : 0;
		return this.max_power;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean powerable() {
		return true;
	}

	/**
	 * This adds the Engine Component's coordinates to the {@link SpaceShip}
	 * @param ship {@link SpaceShip} to which you want to add the Engine component
	 */
	@Override
	public void onCreation(SpaceShip ship, ShipCoords coords) {
		this.coords = coords;
		if (powerable) ship.addPowerableCoords(this.coords);
	}

	/**
	 * This removes the Engine Component's coordinates from the {@link SpaceShip}
	 * @param ship {@link SpaceShip} to which you want to remove the Engine component
	 */
	@Override
	public void onDelete(SpaceShip ship) {
		if (powerable) ship.delPowerableCoords(this.coords);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ClientComponent getClientComponent() {
		ClientComponent c = new ClientEngineComponentDecorator(new ClientBaseComponent(getID(), getRotation(), getConnectors()), getRotation());
		if (!powerable) return c;
		return new ClientPoweredComponentDecorator(c, powered);
	}

}
