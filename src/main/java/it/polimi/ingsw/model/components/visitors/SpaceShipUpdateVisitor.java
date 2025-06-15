package it.polimi.ingsw.model.components.visitors;

import it.polimi.ingsw.model.components.*;
import it.polimi.ingsw.model.components.enums.ShipmentType;

/**
 * Visitor used to update a {@link SpaceShip}'s stats.
 */
public class SpaceShipUpdateVisitor implements ComponentVisitor {

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
	 * Visits a cabin component and fetches the crew count.
	 *
	 * @param c {@link CabinComponent} Component being visited.
	 */
	@Override
	public void visit(CabinComponent c) {
		this.crew_members[c.getCrewType().getArraypos()] += c.getCrew();
	}

	/**
	 * Visit an engine component and fetches its current power.
	 *
	 * @param c {@link EngineComponent} Component being visited.
	 */
	@Override
	public void visit(EngineComponent c) {
		this.engine_power += c.getCurrentPower();
	}

	@Override
	public void visit(AlienLifeSupportComponent c) {
	}

	/**
	 * Visit a Cannon component and fetches the total power of the cannons.
	 *
	 * @param c {@link CannonComponent} Component being visited.
	 */
	@Override
	public void visit(CannonComponent c) {
		this.cannon_power += c.getCurrentPower();
	}

	/**
	 * Visit a Storage component and fetches the count
	 * of containers present in the ship for each valid type.
	 * 
	 * @param c {@link StorageComponent} Component being visited.
	 */
	@Override
	public void visit(StorageComponent c) {
		for (ShipmentType t : ShipmentType.values()) {
			if (t.getValue() < 1) continue;
			this.containers[t.getValue()] += c.howMany(t);
		}
	}

	/**
	 * Visit a Battery component and fetches the number of the batteries.
	 *
	 * @param c {@link BatteryComponent} Component being visited.
	 */
	@Override
	public void visit(BatteryComponent c) {
		this.containers[0] += c.getContains();
	}

	/**
	 * Visits a shield and fetches the directions it is shielding.
	 * 
	 * @param c {@link ShieldComponent} Component being visited.
	 */
	@Override
	public void visit(ShieldComponent c) {
		for (int i = 0; i < 4; i++) {
			this.directions[i] = this.directions[i] || c.getShield().getShielded()[i];
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(EmptyComponent c) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(StructuralComponent c) {
	}

	/**
	 * Visits a StartingCabinComponent and fetches the crew count.
	 * 
	 * @param c {@link StartingCabinComponent} Component being visited.
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
