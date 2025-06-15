package it.polimi.ingsw.model.components.visitors;

import it.polimi.ingsw.model.components.*;
import it.polimi.ingsw.model.components.enums.AlienType;

/**
 * Visitor used to check whether or not a {@link it.polimi.ingsw.model.components.CabinComponent}'s crew dies after a {@link AlienLifeSupportComponent} is destroyed.
 */
public class LifeSupportUpdateVisitor implements ComponentVisitor {

	private final AlienType type;
	private boolean still_alive = false;

	public LifeSupportUpdateVisitor(AlienType type) {
		this.type = type;
	}

	public boolean getStillAlive() {
		return this.still_alive;
	}

	/**
	 * Visits a {@link it.polimi.ingsw.model.components.CabinComponent} and checks if the crew can stay alive.
	 * 
	 * @param c {@link it.polimi.ingsw.model.components.CabinComponent} Component being visited.
	 */
	@Override
	public void visit(CabinComponent c) {
		if (c.getCrewType() != type) still_alive = true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(EngineComponent c) {
	}

	/**
	 * Visits a {@link AlienLifeSupportComponent} and checks what alien type it may support.
	 * 
	 * @param c {@link AlienLifeSupportComponent} Component being visited.
	 */
	@Override
	public void visit(AlienLifeSupportComponent c) {
		if (c.getType() == this.type) still_alive = true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(CannonComponent c) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(StorageComponent c) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(BatteryComponent c) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(ShieldComponent c) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(EmptyComponent c) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(StructuralComponent c) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(StartingCabinComponent c) {
	}

}
