package it.polimi.ingsw.model.cards.visitors;

import it.polimi.ingsw.model.components.*;
import it.polimi.ingsw.model.components.enums.ShipmentType;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.components.visitors.ComponentVisitor;

/**
 * Visitor used to validate moves of {@link it.polimi.ingsw.model.components.enums.ShipmentType cargo} between containers
 */
public class ContainerMoveValidationVisitor implements ComponentVisitor {

	private final ShipmentType searching_for;
	private boolean found = false;

	public ContainerMoveValidationVisitor(ShipmentType type) {
		if (type.getValue() == 0) throw new IllegalArgumentException();
		this.searching_for = type;
	}

	/**
	 * @throws IllegalTargetException always
	 */
	@Override
	public void visit(CabinComponent c) {
		throw new IllegalTargetException();
	}
	/**
	 * @throws IllegalTargetException always
	 */
	@Override
	public void visit(EngineComponent c) {
		throw new IllegalTargetException();
	}
	/**
	 * @throws IllegalTargetException always
	 */
	@Override
	public void visit(AlienLifeSupportComponent c) {
		throw new IllegalTargetException();
	}
	/**
	 * @throws IllegalTargetException always
	 */
	@Override
	public void visit(CannonComponent c) {
		throw new IllegalTargetException();
	}

	/**
	 * Searches if the {@link StorageComponent} contains the wanted cargo type.
	 * 
	 * @param c {@link StorageComponent} Component to visit.
	 * @throws IllegalTargetException if the wanted cargo is not found.
	 */
	@Override
	public void visit(StorageComponent c) {
		if (!found) {
			if (c.howMany(searching_for) == 0) throw new IllegalTargetException();
			this.found = true;
			return;
		}
		if (searching_for.getSpecial() && !c.getSpecial()) throw new IllegalTargetException();
		if (c.getFreeSpaces() == 0) throw new IllegalTargetException();
	}
	/**
	 * @throws IllegalTargetException always
	 */
	@Override
	public void visit(BatteryComponent c) {
		throw new IllegalTargetException();
	}
	/**
	 * @throws IllegalTargetException always
	 */
	@Override
	public void visit(ShieldComponent c) {
		throw new IllegalTargetException();
	}
	/**
	 * @throws IllegalTargetException always
	 */
	@Override
	public void visit(EmptyComponent c) {
		throw new IllegalTargetException();
	}
	/**
	 * @throws IllegalTargetException always
	 */
	@Override
	public void visit(StructuralComponent c) {
		throw new IllegalTargetException();
	}
	/**
	 * @throws IllegalTargetException always
	 */
	@Override
	public void visit(StartingCabinComponent c) {
		throw new IllegalTargetException();
	}

}
