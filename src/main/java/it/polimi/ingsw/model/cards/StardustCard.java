//Done.
package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.state.CardState;
import it.polimi.ingsw.model.cards.state.StardustState;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.state.VoyageState;

/**
 * Represents the "Stardust" card in the game.
 * <p>
 * This card penalizes players who have exposed connectors on their spaceship,
 * causing a loss of time (flight days).
 * Players are resolved in reverse order
 * </p>
 */
public class StardustCard extends Card {

	public StardustCard(int id) {
		super(id, 0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CardState getState(VoyageState state) {
		return new StardustState(state, this);
	}

	/**
	 * Apply the effect of the "Stardust" card to the indicated player:
	 * If the spaceship has exposed connectors, the player will be moved back by the number of exposed connectors.
	 *
	 * @param state The Current State
	 * @param p The Player
	 * @throws NullPointerException    if {@code state} or {@code p} is null
	 */
	public void apply(VoyageState state, Player p) {
		if (state == null || p == null) throw new NullPointerException();
		int lost_days = p.getSpaceShip().countExposedConnectors();
		if (lost_days != 0) state.getPlanche().movePlayer(state, p, -lost_days);
	}

}