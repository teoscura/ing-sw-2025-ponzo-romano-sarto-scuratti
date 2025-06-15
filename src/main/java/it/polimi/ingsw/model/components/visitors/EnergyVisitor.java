package it.polimi.ingsw.model.components.visitors;

import it.polimi.ingsw.model.components.*;

/**
 * Visitor used to turn on/off a component.
 */
public class EnergyVisitor implements ComponentVisitor {

	//If on turns on, if off turns off.
	private boolean positive;

	public EnergyVisitor(boolean on) {
		this.positive = on;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(CabinComponent c) {
	}

	/**
	 * Powers up {@link EngineComponent}.
	 * 
	 * @param c {@link EngineComponent} Component being visited.
	 */
	@Override
	public void visit(EngineComponent c) {
		if (this.positive) {
			c.turnOn();
			return;
		}
		c.turnOff();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(AlienLifeSupportComponent c) {
	}

	/**
	 * Powers up {@link it.polimi.ingsw.model.components.CannonComponent}.
	 * 
	 * @param c {@link it.polimi.ingsw.model.components.CannonComponent} Component being visited.
	 */
	@Override
	public void visit(CannonComponent c) {
		if (this.positive) {
			c.turnOn();
			return;
		}
		c.turnOff();
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
		if (this.positive) {
			c.turnOn();
			return;
		}
		c.turnOff();
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
