package it.polimi.ingsw.model.components.enums;

import it.polimi.ingsw.model.cards.utils.ProjectileDirection;

/**
 * Enumeration that specifies all possible rotations of a component, starting up moving 90 degrees clockwise
 */
public enum ComponentRotation {
	U000(0),
	U090(1),
	U180(2),
	U270(3);

	private final int shift;

	ComponentRotation(int shift) {
		this.shift = shift;
	}

	static public ComponentRotation getRotation(ProjectileDirection dir) {
		switch (dir.getShift()) {
			case 0:
				return ComponentRotation.U000;
			case 1:
				return ComponentRotation.U090;
			case 2:
				return ComponentRotation.U180;
			case 3:
				return ComponentRotation.U270;
		}
		return ComponentRotation.U000;
	}

	public int getShift() {
		return this.shift;
	}

	/**
	 * @return {@link ComponentRotation} The opposite pointing rotation.
	 */
	public ComponentRotation getOpposite() {
		switch (this.shift) {
			case 0:
				return ComponentRotation.U180;
			case 1:
				return ComponentRotation.U270;
			case 2:
				return ComponentRotation.U000;
			case 3:
				return ComponentRotation.U090;
		}
		return ComponentRotation.U000;
	}


}