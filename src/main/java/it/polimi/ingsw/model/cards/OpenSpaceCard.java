package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.state.CardState;
import it.polimi.ingsw.model.cards.state.OpenSpaceState;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.state.VoyageState;

/**
 * Represents the "Open Space" adventure card in the game.
 * <p>
 * This card requires each player to choose whether or not to turn on engines.
 * If a player's spaceship has at least one engine powered on, they will move forward
 * by a number of cells equal to their engine power.
 * Otherwise, the player loses the game.
 * </p>
 */
public class OpenSpaceCard extends Card {

	protected OpenSpaceCard(int id) {
		super(id, 0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CardState getState(VoyageState state) {
		return new OpenSpaceState(state, this);
	}

	/**
	 * Applies the card's effect to the given player's ship.
	 * If the ship has engine power, it moves forward.
	 * Otherwise, the player is eliminated from the game.
	 *
	 * @param state The Current State
	 * @param p The Player
	 * @throws NullPointerException    if {@code state} or {@code p} is null
	 */
	public void apply(VoyageState state, Player p) {
		if (state == null || p == null) throw new NullPointerException();
		if (p.getSpaceShip().getEnginePower() > 0) {
			state.getPlanche().movePlayer(state, p, p.getSpaceShip().getEnginePower());
			return;
		}
		state.loseGame(p);
	}

}
