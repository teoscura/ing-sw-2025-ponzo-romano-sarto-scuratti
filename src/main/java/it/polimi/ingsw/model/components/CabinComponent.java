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
import it.polimi.ingsw.model.components.exceptions.AlienTypeAlreadyPresentException;
import it.polimi.ingsw.model.components.exceptions.UnsupportedAlienCabinException;
import it.polimi.ingsw.model.components.visitors.CabinVisitor;
import it.polimi.ingsw.model.components.visitors.ComponentVisitor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.player.SpaceShip;
/**
 * <h2>CabinComponent</h2>
 * <p>
 * This class models a <strong>crew cabin</strong> on a spaceship.
 * Cabins are used to host human or alien crew members. Each cabin can hold a specific number of crew,
 * depending on the alien type and its capacity.
 * </p>
 */
public class CabinComponent extends BaseComponent {

	private int crew_number = 2;
	private AlienType crew_type = AlienType.HUMAN;

	public CabinComponent(int id,
						  ConnectorType[] connectors,
						  ComponentRotation rotation) {
		super(id, connectors, rotation);
	}

	public CabinComponent(int id,
						  ConnectorType[] connectors,
						  ComponentRotation rotation,
						  ShipCoords coords) {
		super(id, connectors, rotation, coords);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void check(ComponentVisitor v) {
		v.visit(this);
	}

	public int getCrew() {
		return crew_number;
	}

	public AlienType getCrewType() {
		return this.crew_type;
	}

	/**
	 * This method sets the crew in the SpaceShip
	 * @param ship {@link it.polimi.ingsw.model.player.SpaceShip} to which you want set the crew.
	 * @param new_crew the number of crewmates you want to set.
	 * @param type {@link AlienType} the type of aliens you want to set.
	 * @throws NegativeArgumentException if the crew size is zero or negative.
	 * @throws IllegalArgumentException if {@link AlienType} is a collector and not a single alien type.
	 * @throws ArgumentTooBigException if the crew size exceeds {@link AlienType}'s max capacity.
	 * @throws UnsupportedAlienCabinException if the crew type inserted is incompatible with the type in cabin.
	 */
	public void setCrew(SpaceShip ship, int new_crew, AlienType type) {
		if (new_crew < 0) throw new NegativeArgumentException("Crew size can't be negative");
		if (type.getArraypos() < 0)
			throw new IllegalArgumentException("Type must be a single alien type, not a collector");
		if (new_crew > type.getMaxCapacity())
			throw new ArgumentTooBigException("Crew size exceeds type's max capacity");
		if (type.getLifeSupportExists() && ship.getCrew()[type.getArraypos()] > 0)
			throw new AlienTypeAlreadyPresentException("Spaceship already has one alien of this type.");
		CabinVisitor v = new CabinVisitor();
		for (BaseComponent c : this.getConnectedComponents(ship)) {
			c.check(v);
		}
		if (!v.getSupportedType().compatible(type))
			throw new UnsupportedAlienCabinException("Tried to insert crew type in cabin that doesn't support it.");
		crew_number = new_crew;
		crew_type = type;
		ship.updateShip();
	}

	/**
	 * This adds the Cabin Component's coordinates to the {@link it.polimi.ingsw.model.player.SpaceShip}
	 * @param ship {@link it.polimi.ingsw.model.player.SpaceShip} to which you want to add the cabin component
	 */
	@Override
	public void onCreation(SpaceShip ship, ShipCoords coords) {
		this.coords = coords;
		ship.addCabinCoords(this.coords);
	}

	/**
	 * This removes the Cabin Component's coordinates from the {@link it.polimi.ingsw.model.player.SpaceShip}
	 * @param ship {@link it.polimi.ingsw.model.player.SpaceShip} to which you want to remove the cabin component
	 */
	@Override
	public void onDelete(SpaceShip ship) {
		ship.delCabinCoords(this.coords);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ClientComponent getClientComponent() {
		return new ClientCabinComponentDecorator(new ClientBaseComponent(getID(), getRotation(), getConnectors()), crew_type, crew_number, false);
	}
}

