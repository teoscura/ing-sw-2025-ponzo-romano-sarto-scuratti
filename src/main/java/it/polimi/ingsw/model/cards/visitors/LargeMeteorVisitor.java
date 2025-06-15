package it.polimi.ingsw.model.cards.visitors;

import it.polimi.ingsw.model.cards.utils.ProjectileDirection;
import it.polimi.ingsw.model.components.*;
import it.polimi.ingsw.model.components.visitors.ComponentVisitor;

/**
 * Visitor used to check if there are any {@link CannonComponent cannons} pointing in the direction of a large meteor.
 */
public class LargeMeteorVisitor implements ComponentVisitor {

	private final ProjectileDirection d;
	private boolean found_cannon = false;

	public LargeMeteorVisitor(ProjectileDirection d) {
		this.d = d;
	}

	public boolean getFoundCannon() {
		return this.found_cannon;
	}

	/**
	 * Does nothing.
	 */
	@Override
	public void visit(CabinComponent c) {
	}

	/**
	 * Does nothing.
	 */
	@Override
	public void visit(EngineComponent c) {
	}

	/**
	 * Does nothing.
	 */
	@Override
	public void visit(AlienLifeSupportComponent c) {
	}

	/**
	 * Checks if the {@link CannonComponent} is pointed towards the {@link Projectile meteorite}.
	 * 
	 * @param c {@link CannonComponent} Component to be visited.
	 */
	@Override
	public void visit(CannonComponent c) {
		if (c.getCurrentPower() == 0) return;
		if (c.getRotation().getShift() != d.getOpposite().getShift()) return;
		this.found_cannon = true;
	}

	/**
	 * Does nothing.
	 */
	@Override
	public void visit(StorageComponent c) {
	}

	/**
	 * Does nothing.
	 */
	@Override
	public void visit(BatteryComponent c) {
	}

	/**
	 * Does nothing.
	 */
	@Override
	public void visit(ShieldComponent c) {
	}

	/**
	 * Does nothing.
	 */
	@Override
	public void visit(EmptyComponent c) {
	}

	/**
	 * Does nothing.
	 */
	@Override
	public void visit(StructuralComponent c) {
	}

	/**
	 * Does nothing.
	 */
	@Override
	public void visit(StartingCabinComponent c) {
	}

}
