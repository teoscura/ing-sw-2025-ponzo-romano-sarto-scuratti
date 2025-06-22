//Done.
package it.polimi.ingsw.model.components;

import it.polimi.ingsw.model.cards.visitors.CrewRemoveVisitor;
import it.polimi.ingsw.model.client.components.ClientBaseComponent;
import it.polimi.ingsw.model.client.components.ClientComponent;
import it.polimi.ingsw.model.client.components.ClientLifeSupportComponentDecorator;
import it.polimi.ingsw.model.components.enums.AlienType;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.components.visitors.LifeSupportUpdateVisitor;
import it.polimi.ingsw.model.components.visitors.ComponentVisitor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.player.SpaceShip;

import java.util.Arrays;
import java.util.List;

/**
 * <h2>AlienLifeSupportComponent</h2>
 * <p>
 * This class represents a life support system specifically designed to host alien crew members.
 * It is a special type of ship component that allows <b>AlienType</b> aliens (e.g., Brown or Purple)
 * to inhabit cabins connected to it.
 * </p>
 */
public class AlienLifeSupportComponent extends BaseComponent {

	private AlienType type = AlienType.BROWN;

	public AlienLifeSupportComponent(int id,
									 ConnectorType[] connectors,
									 ComponentRotation rotation,
									 AlienType type) {
		super(id, connectors, rotation);
		if (!type.getLifeSupportExists()) {
			throw new IllegalArgumentException();
		}
		this.type = type;
	}

	public AlienLifeSupportComponent(int id,
									 ConnectorType[] connectors,
									 ComponentRotation rotation,
									 AlienType type,
									 ShipCoords coords) {
		super(id, connectors, rotation, coords);
		if (!type.getLifeSupportExists()) {
			throw new IllegalArgumentException();
		}
		this.type = type;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void check(ComponentVisitor v) {
		v.visit(this);
	}

	public AlienType getType() {
		return type;
	}

	/**
	 * This adds the AlienLifeSupport Component's coordinates to the {@link it.polimi.ingsw.model.player.SpaceShip}
	 * 
	 * @param ship {@link it.polimi.ingsw.model.player.SpaceShip} Ship to which you want to add the AlienLifeSupport component
	 */
	@Override
	public void onCreation(SpaceShip ship, ShipCoords coords) {
		this.coords = coords;
	}

	/**
	 * This removes the AlienLifeSupport Component's coordinates from the {@link it.polimi.ingsw.model.player.SpaceShip}
	 * @param ship {@link it.polimi.ingsw.model.player.SpaceShip} Ship to which you want to remove the AlienLifeSupport component
	 */
	@Override
	public void onDelete(SpaceShip ship) {
		BaseComponent[] tmp = this.getConnectedComponents(ship);
		for (BaseComponent c : tmp) {
			if (!ship.isCabin(c.getCoords())) continue;
			LifeSupportUpdateVisitor v = new LifeSupportUpdateVisitor(this.type);
			c.check(v);
			if (v.getStillAlive()) continue;
			List<ShipCoords> to_check = Arrays.asList(c.getConnectedComponents(ship)).stream().map(comp -> comp.getCoords()).toList();
			LifeSupportUpdateVisitor v2 = new LifeSupportUpdateVisitor(this.type);
			for (ShipCoords s : to_check) {
				if (s == this.getCoords()) continue;
				if (ship.isCabin(s)) continue;
				ship.getComponent(s).check(v2);
			}
			if (v2.getStillAlive()) continue;
			CrewRemoveVisitor cr = new CrewRemoveVisitor(ship);
			try {
				c.check(cr);
			} catch (IllegalTargetException e) {
				//crew was already empty, so we ignore this exception;
			}
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ClientComponent getClientComponent() {
		return new ClientLifeSupportComponentDecorator(
				new ClientBaseComponent(this.getID(), this.getRotation(), getConnectors()),
				type);
	}

}



