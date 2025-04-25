package it.polimi.ingsw.model.cards.visitors;

import it.polimi.ingsw.model.cards.utils.ProjectileDirection;
import it.polimi.ingsw.model.components.*;
import it.polimi.ingsw.model.components.visitors.iVisitor;

public class LargeMeteorVisitor implements iVisitor {

	private final ProjectileDirection d;
	private boolean found_cannon = false;

	public LargeMeteorVisitor(ProjectileDirection d) {
		this.d = d;
	}

	public boolean getFoundCannon() {
		return this.found_cannon;
	}

	@Override
	public void visit(CabinComponent c) {
	}

	@Override
	public void visit(EngineComponent c) {
	}

	@Override
	public void visit(AlienLifeSupportComponent c) {
	}

	@Override
	public void visit(CannonComponent c) {
		if (c.getCurrentPower() == 0) return;
		if (c.getRotation().getShift() != d.getOpposite().getShift()) return;
		this.found_cannon = true;
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
