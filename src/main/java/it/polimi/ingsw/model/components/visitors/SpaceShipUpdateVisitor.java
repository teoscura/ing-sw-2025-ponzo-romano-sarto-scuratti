package it.polimi.ingsw.model.components.visitors;

import it.polimi.ingsw.model.components.*;
import it.polimi.ingsw.model.components.enums.ShipmentType;

public class SpaceShipUpdateVisitor implements iVisitor {

	private int battery_power;
	private int engine_power;
	private int cannon_power;
	private final int[] storage_containers;
	private final int[] crew_members;
	private final boolean[] directions;

	public SpaceShipUpdateVisitor() {
		this.storage_containers = new int[4];
		this.crew_members = new int[3];
		this.directions = new boolean[4];
	}

	@Override
	public void visit(CabinComponent c) {
		this.crew_members[c.getCrewType().getArraypos()] += c.getCrew();
	}

	@Override
	public void visit(EngineComponent c) {
		this.engine_power += c.getCurrentPower();
	}

	@Override
	public void visit(AlienLifeSupportComponent c) {
	}

	@Override
	public void visit(CannonComponent c) {
		this.cannon_power += c.getCurrentPower();
	}

	@Override
	public void visit(StorageComponent c) {
		for (ShipmentType t : ShipmentType.values()) {
			if (t.getValue() < 1) continue;
			this.storage_containers[t.getValue() - 1] = c.howMany(t);
		}
	}

	@Override
	public void visit(BatteryComponent c) {
		this.battery_power += c.getContains();
	}

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

	@Override
	public void visit(StartingCabinComponent c) {
		this.crew_members[0] += c.getCrew();
	}

	public int getBatteryPower() {
		return this.battery_power;
	}

	public int getEnginePower() {
		return this.engine_power;
	}

	public int getCannonPower() {
		return this.cannon_power;
	}

	public int[] getStorageContainers() {
		return this.storage_containers;
	}

	public int[] getCrewMembers() {
		return this.crew_members;
	}

	public boolean[] getDirections() {
		return this.directions;
	}

}
