//Done.
package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.components.BaseComponent;
import it.polimi.ingsw.model.components.ComponentFactory;
import it.polimi.ingsw.model.components.exceptions.ContainerEmptyException;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * CommonBoard handles the component piles durning the building phase of the game.
 * Contains a queue of components not yet revealed, and an array of uncovered components,
 * accessed directly and visible by the players
 */
public class CommonBoard implements iCommonBoard {

	private final ArrayDeque<BaseComponent> covered_components;
	private final HashMap<Integer, BaseComponent> discarded_components;

	public CommonBoard() {
		ComponentFactory factory = new ComponentFactory();
		ArrayList<BaseComponent> tmp = new ArrayList<>();
		main:
		for (int i = 1; i < 158; i++) {
			for (int t : factory.getForbiddenID()) {
				if (t == i) continue main;
			}
			tmp.add(factory.getComponent(i));
		}
		Collections.shuffle(tmp);
		this.covered_components = new ArrayDeque<BaseComponent>(tmp);
		this.discarded_components = new HashMap<>();
	}

	/**
	 * Returns the current first component of the pile
	 *
	 * @return the first element of the pile
	 */
	@Override
	public BaseComponent pullComponent() {
		if (this.covered_components.isEmpty()) return null;
		return this.covered_components.poll();
	}

	/**
	 * Takes a component c that has been discarded by the player and adds it to the uncovered_components array
	 *
	 * @throws IllegalArgumentException if a component is discarded twice
	 * @param c
	 */
	@Override
	public void discardComponent(BaseComponent c) {
		if (this.discarded_components.containsKey(c.getID()))
			throw new IllegalArgumentException("Tried to insert a duplicate component.");
		this.discarded_components.put(c.getID(), c);
	}

	/**
	 * Pulls a component from the uncovered_components array with direct access
	 *
	 * @param id The Id
	 * @return
	 */
	@Override
	public BaseComponent pullDiscarded(int id) {
		if (!this.discarded_components.containsKey(id)) throw new ContainerEmptyException();
		BaseComponent tmp = this.discarded_components.get(id);
		this.discarded_components.remove(id);
		return tmp;
	}

	@Override
	public int getCoveredSize() {
		return this.covered_components.size();
	}

	@Override
	public ArrayList<Integer> getDiscarded() {
		return new ArrayList<Integer>(this.discarded_components.keySet());
	}

}
