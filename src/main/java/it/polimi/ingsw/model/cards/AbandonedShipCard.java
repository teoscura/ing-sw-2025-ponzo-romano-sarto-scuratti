//Done.
package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.state.AbandonedShipAnnounceState;
import it.polimi.ingsw.model.cards.state.CardState;
import it.polimi.ingsw.model.cards.utils.CardOrder;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.state.VoyageState;

/**
 * Represents an "Abandoned Ship" card in the game.
 * <p>
 * This card allows the player to land on an abandoned ship to get credits,
 * at the cost of losing crew members and travel days.
 * </p>
 */
public class AbandonedShipCard extends Card {

	private final int credits_gained;
	private final int crew_lost;

	public AbandonedShipCard(int id, int days, int crew_lost, int credits_gained) {
		super(id, days);
		if (crew_lost <= 0 || credits_gained <= 0)
			throw new IllegalArgumentException("Negative arguments not allowed.");
		this.crew_lost = crew_lost;
		this.credits_gained = credits_gained;
	}

	@Override
	public CardState getState(VoyageState state) {
		return new AbandonedShipAnnounceState(state, this, state.getOrder(CardOrder.NORMAL));
	}

	public int getCredits() {
		return this.credits_gained;
	}

	public int getCrewLost() {
		return this.crew_lost;
	}

	/**
	 * <p><strong>Only one player can use this opportunity.</strong>
	 * The leader decides first. The player can waive the indicated number of
	 * crew tokens (human and/or alien) and take the specified number of credits.
	 * This also costs a certain number of flight days.</p>
	 *
	 * <p>If the leader chooses not to take advantage of the opportunity,
	 * he can do it the player immediately after in order of route and so on.
	 * Once someone decides to repair the abandoned ship, the remaining players are cut off.</p>
	 *
	 * @param state The Current State
	 * @param p The Player
	 * @param id The Id
	 * @throws IllegalArgumentException if the player does not have enough crew
	 * @throws NullPointerException    if state or p is null
	 */
	public void apply(VoyageState state, Player p, int id) {
		if (state == null || p == null) throw new NullPointerException();
		if (id == 0) {
			if (p.getSpaceShip().getTotalCrew() <= this.crew_lost)
				throw new IllegalArgumentException("The crew isn't big enough for this abandoned ship.");
			this.exhaust();
		}
	}

}