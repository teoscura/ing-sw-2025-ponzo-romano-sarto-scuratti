package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.components.BaseComponent;
import it.polimi.ingsw.model.components.exceptions.ContainerEmptyException;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Interface representing the center board during the construction phase.
 */
public interface iCommonBoard extends Serializable {

	int getCoveredSize();

	/**
	 * Returns the current first component of the pile
	 *
	 * @return {@link it.polimi.ingsw.model.components.BaseComponent}  The first element of the pile
	 */
	BaseComponent pullComponent();

	/**
	 * Takes a component c that has been discarded by the player and adds it to the uncovered_components array
	 *
	 * @param c {@link it.polimi.ingsw.model.components.BaseComponent}  Component to discard
	 * @throws IllegalArgumentException if a component is discarded twice
	 */
	void discardComponent(BaseComponent c);

	/**
	 * Pulls a component from the uncovered_components array with direct access
	 *
	 * @param id The Id
	 * @return {@link it.polimi.ingsw.model.components.BaseComponent}  Component pulled.
	 * @throws ContainerEmptyException if the ID is not contained inside the discarded componentts.
	 */
	BaseComponent pullDiscarded(int id);

	ArrayList<Integer> getDiscarded();
}
