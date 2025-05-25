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
import it.polimi.ingsw.model.components.visitors.iVisitor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.player.SpaceShip;

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

	@Override
	public void check(iVisitor v) {
		v.visit(this);
	}

	public int getCrew() {
		return crew_number;
	}

	public AlienType getCrewType() {
		return this.crew_type;
	}

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

	@Override
	public void onCreation(SpaceShip ship, ShipCoords coords) {
		this.coords = coords;
		ship.addCabinCoords(this.coords);
	}

	@Override
	public void onDelete(SpaceShip ship) {
		ship.delCabinCoords(this.coords);
	}

	@Override
	public ClientComponent getClientComponent() {
		return new ClientCabinComponentDecorator(new ClientBaseComponent(getID(), getRotation(), getConnectors()), crew_type, crew_number, false);
	}
}

