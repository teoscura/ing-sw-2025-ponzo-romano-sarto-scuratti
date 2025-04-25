package it.polimi.ingsw.model.components.visitors;

import it.polimi.ingsw.model.components.*;

public interface iVisitor {

	void visit(CabinComponent c);

	void visit(EngineComponent c);

	void visit(AlienLifeSupportComponent c);

	void visit(CannonComponent c);

	void visit(StorageComponent c);

	void visit(BatteryComponent c);

	void visit(ShieldComponent c);

	void visit(EmptyComponent c);

	void visit(StructuralComponent c);

	void visit(StartingCabinComponent c);
}

