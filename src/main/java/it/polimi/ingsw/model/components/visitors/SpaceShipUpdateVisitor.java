package it.polimi.ingsw.model.components.visitors;

import it.polimi.ingsw.model.components.*;
import it.polimi.ingsw.model.components.enums.ShipmentType;

public class SpaceShipUpdateVisitor implements iVisitor {

	private final int[] containers;
	private final int[] crew_members;
	private final boolean[] directions;
	private int engine_power;
	private double cannon_power;

	public SpaceShipUpdateVisitor() {
		this.containers = new int[5];
		this.crew_members = new int[3];
		this.directions = new boolean[4];
	}

	/**
	 * Visit a cabin component and update the crew count.
	 *
	 * @param c CabinComponent component containing crew members of a specific type.
	 */
	@Override
	public void visit(CabinComponent c) {
		this.crew_members[c.getCrewType().getArraypos()] += c.getCrew();
	}

	/**
	 * Visit an engine component and update the total power of the engines.
	 *
	 * @param c EngineComponent.
	 */
	@Override
	public void visit(EngineComponent c) {
		this.engine_power += c.getCurrentPower();
	}

	@Override
	public void visit(AlienLifeSupportComponent c) {
	}

	/**
	 * Visit a Cannon component and update the total power of the cannons.
	 *
	 * @param c CannonComponent.
	 */
	@Override
	public void visit(CannonComponent c) {
		this.cannon_power += c.getCurrentPower();
	}

	/**
	 * Visit a Storage component and update the count
	 * of containers present in the ship for each valid type.
	 * @param c CannonComponent.
	 */
	@Override
	public void visit(StorageComponent c) {
		for (ShipmentType t : ShipmentType.values()) {
			if (t.getValue() < 1) continue;
			this.containers[t.getValue()] += c.howMany(t);
		}
	}

	/**
	 * Visit a Battery component and update the total power of the batteries.
	 *
	 * @param c BatteryComponent.
	 */
	@Override
	public void visit(BatteryComponent c) {
		this.containers[0] += c.getContains();
	}

	/**
	 * Visits a shield and updates the directions.
	 * @param c componente ShieldComponent.
	 */
	@Override
	public void visit(ShieldComponent c) {
		for (int i = 0; i < 4; i++) {
			this.directions[i] = this.directions[i] || c.getShield().getShielded()[i];
		}
	}

	@Override
	public void visit(EmptyComponent c) {
	}

	@Override
	public void visit(StructuralComponent c) {
	}

	/**
	 * Visits a StartingCabinComponent and updates the crew count.
	 * @param c StartingCabinComponent containing humans.
	 */
	@Override
	public void visit(StartingCabinComponent c) {
		this.crew_members[0] += c.getCrew();
	}

	public int getEnginePower() {
		return this.engine_power;
	}

	public double getCannonPower() {
		return this.cannon_power;
	}

	public int[] getContainers() {
		return this.containers;
	}

	public int[] getCrewMembers() {
		return this.crew_members;
	}

	public boolean[] getDirections() {
		return this.directions;
	}

}
