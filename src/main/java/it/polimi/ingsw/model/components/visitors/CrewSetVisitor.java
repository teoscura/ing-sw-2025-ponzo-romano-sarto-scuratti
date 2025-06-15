package it.polimi.ingsw.model.components.visitors;

import it.polimi.ingsw.model.components.*;
import it.polimi.ingsw.model.components.enums.AlienType;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.player.SpaceShip;

/**
 * Visitor used to set a {@link it.polimi.ingsw.model.components.CabinComponent}'s crew type.
 */
public class CrewSetVisitor implements ComponentVisitor {

	private final SpaceShip ship;
	private final AlienType type;

	public CrewSetVisitor(SpaceShip ship, AlienType type) {
		if (ship == null) throw new NullPointerException();
		if (type.getArraypos() < 0 && type.getMaxCapacity() > 0) throw new IllegalArgumentException();
		this.ship = ship;
		this.type = type;
	}

	/**
	 * Sets the crew of a {@link it.polimi.ingsw.model.components.CabinComponent} with the {@link AlienType type} currently set in the visitor.
	 * 
	 * @param c {@link it.polimi.ingsw.model.components.CabinComponent} Component being visited.
	 */
	@Override
	public void visit(CabinComponent c) {
		c.setCrew(ship, type.getMaxCapacity(), type);
		ship.updateShip();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(EngineComponent c) {
		throw new IllegalTargetException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(AlienLifeSupportComponent c) {
		throw new IllegalTargetException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(CannonComponent c) {
		throw new IllegalTargetException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(StorageComponent c) {
		throw new IllegalTargetException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(BatteryComponent c) {
		throw new IllegalTargetException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(ShieldComponent c) {
		throw new IllegalTargetException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(EmptyComponent c) {
		throw new IllegalTargetException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(StructuralComponent c) {
		throw new IllegalTargetException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(StartingCabinComponent c) {
		throw new IllegalTargetException();
	}

}
