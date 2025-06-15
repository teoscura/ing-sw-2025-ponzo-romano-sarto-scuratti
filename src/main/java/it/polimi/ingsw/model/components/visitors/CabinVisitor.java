package it.polimi.ingsw.model.components.visitors;

import it.polimi.ingsw.model.components.*;
import it.polimi.ingsw.model.components.enums.AlienType;

/**
 * Visitor used to verify what {@link AlienType} a CabinComponent can support.
 */
public class CabinVisitor implements ComponentVisitor {

	AlienType type = AlienType.HUMAN;

	public AlienType getSupportedType() {
		return this.type;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(CabinComponent c) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(EngineComponent c) {
	}

	/**
	 * Visits a StartingCabinComponent and fetches the crew count.
	 * 
	 * @param c {@link StartingCabinComponent} Component being visited.
	 */
	@Override
	public void visit(AlienLifeSupportComponent c) {
		if (this.type == AlienType.BOTH) return;
		if (this.type == AlienType.HUMAN) {
			this.type = c.getType();
			return;
		}
		if (this.type != c.getType()) {
			this.type = AlienType.BOTH;
		}
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

