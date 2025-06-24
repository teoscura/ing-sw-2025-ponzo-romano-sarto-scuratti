//Done.
package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.state.CardState;
import it.polimi.ingsw.model.cards.state.EpidemicState;
import it.polimi.ingsw.model.cards.visitors.CrewRemoveVisitor;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.VoyageState;

import java.util.ArrayList;

/**
 * Represents the "Epidemic" adventure card in the game.
 * <p>
 * This card simulates the outbreak of an illness spreading through connected cabins
 * in a player's spaceship. Crew members in affected cabins are removed. If no crew
 * members survive, the player loses the game.
 * </p>
 */
public class EpidemicCard extends Card {

	public EpidemicCard(int id) {
		super(id, 0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CardState getState(VoyageState state) {
		return new EpidemicState(state, this);
	}

	/**
	 * Applies the effect of the epidemic to the player's ship.
	 * <p>
	 * The method identifies all connected cabins and removes their crew using
	 * a {@link CrewRemoveVisitor}. If the ship has no crew left after this,
	 * the player is considered to have lost the game.
	 * </p>
	 *
	 * @throws NullPointerException    if {@code state} or {@code p} is null
	 */
	public void apply(VoyageState state, Player p) {
		if (state == null || p == null) throw new NullPointerException();
		ArrayList<ShipCoords> ill_cabins = p.getSpaceShip().findConnectedCabins();
		CrewRemoveVisitor v = new CrewRemoveVisitor(p.getSpaceShip());
		for (ShipCoords s : ill_cabins) {
			try {
				p.getSpaceShip().getComponent(s).check(v);
			} catch (IllegalTargetException e) {
				//Do nothing, if its empty its fine.
			}
		}
		p.getSpaceShip().updateShip();
	}

}
