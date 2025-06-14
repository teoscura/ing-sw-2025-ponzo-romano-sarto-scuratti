//Done.
package it.polimi.ingsw.model.components.visitors;

import it.polimi.ingsw.model.components.*;

//This exists just to not do a `instanceof EmptySpace`.
public class FreeSpaceVisitor implements ComponentVisitor {

	private boolean isfree = true;

	@Override
	public void visit(CabinComponent c) {
		this.isfree = false;
	}

	@Override
	public void visit(EngineComponent c) {
		this.isfree = false;
	}

	@Override
	public void visit(AlienLifeSupportComponent c) {
		this.isfree = false;
	}

	@Override
	public void visit(CannonComponent c) {
		this.isfree = false;
	}

	@Override
	public void visit(StorageComponent c) {
		this.isfree = false;
	}

	@Override
	public void visit(BatteryComponent c) {
		this.isfree = false;
	}

	@Override
	public void visit(ShieldComponent c) {
		this.isfree = false;
	}

	@Override
	public void visit(EmptyComponent c) {
		this.isfree = true;
	}

	@Override
	public void visit(StructuralComponent structuralComponent) {
		this.isfree = false;
	}

	@Override
	public void visit(StartingCabinComponent c) {
		this.isfree = false;
	}

	public void reset() {
		this.isfree = false;
	}

	public boolean getSpaceIsFree() {
		return this.isfree;
	}

}