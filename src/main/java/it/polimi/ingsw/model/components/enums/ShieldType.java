package it.polimi.ingsw.model.components.enums;

public enum ShieldType {
	NE(true, true, false, false),
	SE(false, true, true, false),
	SW(false, false, true, true),
	NW(true, false, false, true),
	NONE(false, false, false, false);

	private final boolean[] shielded;

	ShieldType(boolean n, boolean e, boolean s, boolean w) {
		this.shielded = new boolean[4];
		this.shielded[0] = n;
		this.shielded[1] = e;
		this.shielded[2] = s;
		this.shielded[3] = w;
	}

	public boolean getNorth() {
		return this.shielded[0];
	}

	public boolean getEast() {
		return this.shielded[1];
	}

	public boolean getSouth() {
		return this.shielded[2];
	}

	public boolean getWest() {
		return this.shielded[3];
	}

	public boolean[] getShielded() {
		return this.shielded;
	}
}