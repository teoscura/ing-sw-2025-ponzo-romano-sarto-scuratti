package it.polimi.ingsw.model.components.visitors;

import it.polimi.ingsw.model.components.*;
import it.polimi.ingsw.model.components.enums.AlienType;

public class CabinVisitor implements iVisitor {

	AlienType type = AlienType.HUMAN;

	public AlienType getSupportedType() {
		return this.type;
	}

	@Override
	public void visit(CabinComponent c) {
	}

	@Override
	public void visit(EngineComponent c) {
	}

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

	@Override
	public void visit(CannonComponent c) {
	}

	@Override
	public void visit(StorageComponent c) {
	}

	@Override
	public void visit(BatteryComponent c) {
	}

	@Override
	public void visit(ShieldComponent c) {
	}

	@Override
	public void visit(EmptyComponent c) {
	}

	@Override
	public void visit(StructuralComponent c) {
	}

	@Override
	public void visit(StartingCabinComponent c) {
	}
}

