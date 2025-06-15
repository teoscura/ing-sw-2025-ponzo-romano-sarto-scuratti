//Done.
package it.polimi.ingsw.model.cards.visitors;

import it.polimi.ingsw.model.components.*;
import it.polimi.ingsw.model.components.enums.AlienType;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.components.visitors.ComponentVisitor;
import it.polimi.ingsw.model.player.SpaceShip;

/**
 * Visitor used to remove crew from any types of {@link it.polimi.ingsw.model.components.CabinComponent}.
 */
public class CrewRemoveVisitor implements ComponentVisitor {

	private final SpaceShip ship;

	public CrewRemoveVisitor(SpaceShip ship) {
		if (ship == null) throw new NullPointerException();
		this.ship = ship;
	}

	/**
	 * Tries to remove crew from a {@link it.polimi.ingsw.model.components.CabinComponent}.
	 * 
	 * @param c {@link StartingCabinComponent} Component to visit.
	 * @throws IllegalTargetException if the {@link it.polimi.ingsw.model.components.CabinComponent} doesn't contain any crew.
	 */
	@Override
	public void visit(CabinComponent c) {
		if (c.getCrew() == 0) throw new IllegalTargetException("Coords don't correspond to a inhabited cabin");
		if (!c.getCrewType().getLifeSupportExists()) c.setCrew(this.ship, c.getCrew() - 1, AlienType.HUMAN);
		else c.setCrew(this.ship, 0, AlienType.HUMAN);
		ship.updateShip();
	}

	/**
	 * @throws IllegalTargetException always
	 */
	@Override
	public void visit(EngineComponent c) {
		throw new IllegalTargetException("Coords don't correspond to a cabin");
	}

	/**
	 * @throws IllegalTargetException always
	 */
	@Override
	public void visit(AlienLifeSupportComponent c) {
		throw new IllegalTargetException("Coords don't correspond to a cabin");
	}

	/**
	 * @throws IllegalTargetException always
	 */
	@Override
	public void visit(CannonComponent c) {
		throw new IllegalTargetException("Coords don't correspond to a cabin");
	}

	/**
	 * @throws IllegalTargetException always
	 */
	@Override
	public void visit(StorageComponent c) {
		throw new IllegalTargetException("Coords don't correspond to a cabin");
	}

	/**
	 * @throws IllegalTargetException always
	 */
	@Override
	public void visit(BatteryComponent c) {
		throw new IllegalTargetException("Coords don't correspond to a cabin");
	}

	/**
	 * @throws IllegalTargetException always
	 */
	@Override
	public void visit(ShieldComponent c) {
		throw new IllegalTargetException("Coords don't correspond to a cabin");
	}

	/**
	 * @throws IllegalTargetException always
	 */
	@Override
	public void visit(EmptyComponent c) {
		throw new IllegalTargetException("Coords don't correspond to a cabin");
	}

	/**
	 * @throws IllegalTargetException always
	 */
	@Override
	public void visit(StructuralComponent c) {
		throw new IllegalTargetException("Coords don't correspond to a cabin");
	}

	/**
	 * Tries to remove crew from a {@link StartingCabinComponent}.
	 * 
	 * @param c {@link StartingCabinComponent} Component to visit.
	 * @throws IllegalTargetException if the {@link StartingCabinComponent} doesn't contain any crew.
	 */
	@Override
	public void visit(StartingCabinComponent c) {
		if (c.getCrew() == 0) throw new IllegalTargetException("Coords don't correspond to a inhabited cabin");
		c.setCrew(this.ship, c.getCrew() - 1, AlienType.HUMAN);
		ship.updateShip();
	}

}
