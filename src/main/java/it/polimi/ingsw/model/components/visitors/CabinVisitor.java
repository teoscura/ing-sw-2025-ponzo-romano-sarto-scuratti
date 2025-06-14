package it.polimi.ingsw.model.components.visitors;

import it.polimi.ingsw.model.components.*;
import it.polimi.ingsw.model.components.enums.AlienType;

public class CabinVisitor implements ComponentVisitor {

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

	/**
	 * Visit an Alien Life Support Component and update the visitor's internal status
	 * based on the alien type.<br>
	 * - If the visitor's current type is BOTH, no change is made. <br>
	 * - If the current type is HUMAN, it is overwritten with the type of the visited component. <br>
	 * - If the current type is different from the component's type, it is set to BOTH to indicate that <br>
	 *
	 * @param c
	 */
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

