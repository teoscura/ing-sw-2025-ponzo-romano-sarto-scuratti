//Done.
package it.polimi.ingsw.model.components;

import it.polimi.ingsw.exceptions.ArgumentTooBigException;
import it.polimi.ingsw.exceptions.NegativeArgumentException;
import it.polimi.ingsw.model.client.components.ClientBaseComponent;
import it.polimi.ingsw.model.client.components.ClientCabinComponentDecorator;
import it.polimi.ingsw.model.client.components.ClientComponent;
import it.polimi.ingsw.model.components.enums.AlienType;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import it.polimi.ingsw.model.components.visitors.iVisitor;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.player.SpaceShip;

/**
 * <h2>StartingCabinComponent</h2>
 * <p>
 * Represents the <strong>starting (central) cabin</strong> of the spaceship in Galaxy Trucker.
 * Each ship begins with exactly one of these, and it is the core structural and functional center of the vessel.
 * </p>
 */
public class StartingCabinComponent extends BaseComponent {

	private final PlayerColor color;
	private int crew_number;

	public StartingCabinComponent(int id,
								  ConnectorType[] connectors,
								  ComponentRotation rotation,
								  PlayerColor color) {
		super(id, connectors, rotation);
		if (color.getOrder() < 0) throw new IllegalArgumentException("Color can't be \"NONE\".");
		this.color = color;
		this.crew_number = 2;
	}

	public StartingCabinComponent(int id,
								  ConnectorType[] connectors,
								  ComponentRotation rotation,
								  PlayerColor color,
								  ShipCoords coords) {
		super(id, connectors, rotation, coords);
		if (color.getOrder() < 0) throw new IllegalArgumentException("Color can't be \"NONE\".");
		this.color = color;
		this.crew_number = 2;
	}

	@Override
	public void check(iVisitor v) {
		v.visit(this);
	}

	public int getCrew() {
		return crew_number;
	}

	public AlienType getCrewType() {
		return AlienType.HUMAN;
	}

	public PlayerColor getColor() {
		return this.color;
	}

	/**
	 * Set the number of crew members in the Starting Cabin
	 * Must be human and must meet maximum capacity limits.
	 *
	 * @param ship  {@link SpaceShip}
	 * @param new_crew  Number of crew members to be assigned.
	 * @param type      Alien type (must be {@link AlienType#HUMAN}).
	 * @throws NegativeArgumentException    Crew size can't be zero or negative
	 * @throws IllegalArgumentException     Central cabin can only contain humans
	 * @throws ArgumentTooBigException      Crew size exceeds type's max capacity
	 */
	public void setCrew(SpaceShip ship, int new_crew, AlienType type) {
		if (new_crew < 0) throw new NegativeArgumentException("Crew size can't be zero or negative");
		if (type != AlienType.HUMAN) throw new IllegalArgumentException("Central cabin can only contain humans");
		if (new_crew > AlienType.HUMAN.getMaxCapacity())
			throw new ArgumentTooBigException("Crew size exceeds type's max capacity");
		crew_number = new_crew;
	}

	/**
	 * This adds the StartingCabin Component's coordinates to the {@link SpaceShip}
	 * @param ship  {@link SpaceShip} to which you want to add the StartingCabin component
	 */
	@Override
	public void onCreation(SpaceShip ship, ShipCoords coords) {
		this.coords = coords;
		ship.addCabinCoords(this.coords);
	}

	/**
	 * This removes the StartingCabin Component's coordinates from the {@link SpaceShip}
	 * @param ship  {@link SpaceShip} to which you want to remove the StartingCabin component
	 */
	@Override
	public void onDelete(SpaceShip ship) {
		ship.delCabinCoords(this.coords);
	}

	@Override
	public ClientComponent getClientComponent() {
		return new ClientCabinComponentDecorator(new ClientBaseComponent(getID(), getRotation(), getConnectors()), AlienType.HUMAN, crew_number, true);
	}
}
