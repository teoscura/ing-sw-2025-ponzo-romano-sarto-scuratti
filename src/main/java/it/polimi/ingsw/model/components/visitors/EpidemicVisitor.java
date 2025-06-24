package it.polimi.ingsw.model.components.visitors;

import it.polimi.ingsw.model.components.*;

public class EpidemicVisitor implements ComponentVisitor{

	private boolean result = false;

	public void visit(CabinComponent component) {
		result = component.getCrew() > 0;
	}

	@Override
	public void visit(EngineComponent c) {
	}

	@Override
	public void visit(AlienLifeSupportComponent c) {
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

	public void visit(StartingCabinComponent component) {
		result = component.getCrew() > 0;
	}

	public boolean getResult() {
		return result;
	}

}
