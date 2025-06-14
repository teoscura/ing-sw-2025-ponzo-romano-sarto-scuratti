package it.polimi.ingsw.model.cards.visitors;

import it.polimi.ingsw.model.components.*;
import it.polimi.ingsw.model.components.enums.ShipmentType;
import it.polimi.ingsw.model.components.exceptions.ContainerEmptyException;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.components.visitors.ComponentVisitor;
import it.polimi.ingsw.model.player.SpaceShip;

/**
 * Visitor used to remove cargo and batteries from {@link BatteryComponent} and {@link StorageComponent}.
 */
public class ContainsRemoveVisitor implements ComponentVisitor {

	private final SpaceShip ship;
	private ShipmentType searching;

	public ContainsRemoveVisitor(SpaceShip ship, ShipmentType type) {
		if (ship == null || type == null) throw new NullPointerException();
		this.ship = ship;
		this.searching = type;
	}

	public void changeType(ShipmentType type) {
		if (type == null) throw new NullPointerException();
		this.searching = type;
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
	 * Tries to remove the wanted cargo type from a {@link StorageComponent}.
	 * 
	 * @param c {@link StorageComponent} Component to visit.
	 * @throws IllegalTargetException if the visitor is trying to remove batteries.
	 * @throws ContainerEmptyException if the {@link StorageComponent} doesn't contain any of the wanted cargo type.
	 */
	@Override
	public void visit(StorageComponent c) {
		if (this.searching == ShipmentType.EMPTY) throw new IllegalTargetException();
		if (c.howMany(searching) <= 0) throw new ContainerEmptyException();
		c.takeOut(searching);
		ship.updateShip();
	}

	/**
	 * Tries to remove batteries from a {@link BatteryComponent}.
	 * 
	 * @param c {@link BatteryComponent} Component to visit.
	 * @throws IllegalTargetException if the visitor is trying to remove cargo.
	 * @throws ContainerEmptyException if the {@link BatteryComponent} doesn't contain any batteries.
	 */
	@Override
	public void visit(BatteryComponent c) {
		if (this.searching != ShipmentType.EMPTY) throw new IllegalTargetException();
		if (c.getContains() > 0) {
			c.takeOne();
			ship.updateShip();
			return;
		}
		throw new ContainerEmptyException();
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
