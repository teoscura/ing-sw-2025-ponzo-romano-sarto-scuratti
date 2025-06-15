package it.polimi.ingsw.model.components.visitors;

import it.polimi.ingsw.model.components.*;

/**
 * Visitor interface that allows any implementer to distinguish between the various subclasses of {@link it.polimi.ingsw.model.components.BaseComponent} .
 */
public interface ComponentVisitor {

	/**
	 * Does nothing and returns immediately.
	 * @param c {@link it.polimi.ingsw.model.components.CabinComponent} Component being visited.
	 */
	void visit(CabinComponent c);
	/**
	 * Does nothing and returns immediately.
	 * @param c {@link EngineComponent} Component being visited.
	 */
	void visit(EngineComponent c);
	/**
	 * Does nothing and returns immediately.
	 * @param c {@link AlienLifeSupportComponent} Component being visited.
	 */
	void visit(AlienLifeSupportComponent c);
	/**
	 * Does nothing and returns immediately.
	 * @param c {@link it.polimi.ingsw.model.components.CannonComponent} Component being visited.
	 */
	void visit(CannonComponent c);
	/**
	 * Does nothing and returns immediately.
	 * @param c {@link StorageComponent} Component being visited.
	 */
	void visit(StorageComponent c);
	/**
	 * Does nothing and returns immediately.
	 * @param c {@link BatteryComponent} Component being visited.
	 */
	void visit(BatteryComponent c);
	/**
	 * Does nothing and returns immediately.
	 * @param c {@link ShieldComponent} Component being visited.
	 */
	void visit(ShieldComponent c);
	/**
	 * Does nothing and returns immediately.
	 * @param c {@link EmptyComponent} Component being visited.
	 */
	void visit(EmptyComponent c);
	/**
	 * Does nothing and returns immediately.
	 * @param c {@link StructuralComponent} Component being visited.
	 */
	void visit(StructuralComponent c);
	/**
	 * Does nothing and returns immediately.
	 * @param c {@link StartingCabinComponent} Component being visited.
	 */
	void visit(StartingCabinComponent c);
}

