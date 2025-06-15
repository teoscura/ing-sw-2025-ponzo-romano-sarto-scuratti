//Done.
package it.polimi.ingsw.model.cards.utils;

/**
 * Enumeration that specifies all possible rotations of a projectile, starting up moving 90 degrees clockwise
 */
public enum ProjectileDirection {
	U000(0),
	U090(1),
	U180(2),
	U270(3);

	private final int shift;

	ProjectileDirection(int shift) {
		this.shift = shift;
	}

	public int getShift() {
		return this.shift;
	}

	public ProjectileDirection getOpposite() {
		switch (this.shift) {
			case 0:
				return ProjectileDirection.U180;
			case 1:
				return ProjectileDirection.U270;
			case 2:
				return ProjectileDirection.U000;
			case 3:
				return ProjectileDirection.U090;
			default:
				return ProjectileDirection.U000;
		}
	}
}