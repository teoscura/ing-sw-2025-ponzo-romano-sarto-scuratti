//Done.
package it.polimi.ingsw.model.cards.utils;

import it.polimi.ingsw.exceptions.OutOfBoundsException;

import java.io.Serializable;

public class Projectile implements Serializable {

	private final ProjectileDirection direction;
	private final ProjectileDimension dimension;
	private final int offset;

	public Projectile(ProjectileDirection direction, ProjectileDimension dimension) {
		this.direction = direction;
		this.dimension = dimension;
		this.offset = -1;
	}

	public Projectile(ProjectileDirection direction, ProjectileDimension dimension, int offset) {
		if (offset < 1 || offset > 12) throw new OutOfBoundsException("Offset goes over ship bounds.");
		this.offset = offset;
		this.direction = direction;
		this.dimension = dimension;
	}

	public ProjectileDirection getDirection() {
		return this.direction;
	}

	public ProjectileDimension getDimension() {
		return this.dimension;
	}

	public int getOffset() {
		return this.offset;
	}
} 




