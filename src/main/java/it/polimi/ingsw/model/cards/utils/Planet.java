//Done.
package it.polimi.ingsw.model.cards.utils;

import it.polimi.ingsw.exceptions.NegativeArgumentException;
import it.polimi.ingsw.model.cards.exceptions.AlreadyVisitedException;

import java.io.Serializable;

/**
 * Class representing a single landing place on a {@link it.polimi.ingsw.model.cards.PlanetCard}.
 */
public class Planet implements Serializable {

	private final int[] contains;
	private final int id;
	private boolean visited = false;

	public Planet(int id, int[] contains) {
		if (contains.length != 4 || id < 0)
			throw new IllegalArgumentException("Array provided doesn't match number of possible shipments.");
		for (int t : contains) {
			if (t < 0) throw new NegativeArgumentException("Container quantity can't be less than zero.");
		}
		this.id = id;
		this.contains = contains;
	}

	public int[] getContains() {
		return this.contains;
	}

	public int getID(){
		return id;
	}

	public void visit() {
		if (this.visited) throw new AlreadyVisitedException();
		this.visited = true;
	}

	public boolean getVisited() {
		return this.visited;
	}

	public int getTotalContains() {
		int sum = 0;
		for (int i : this.contains) {
			sum += i;
		}
		return sum;
	}

}
