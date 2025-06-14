//Done.
package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.exceptions.NegativeArgumentException;
import it.polimi.ingsw.model.cards.exceptions.CrewSizeException;
import it.polimi.ingsw.model.cards.state.AbandonedStationAnnounceState;
import it.polimi.ingsw.model.cards.state.CardState;
import it.polimi.ingsw.model.cards.utils.CardOrder;
import it.polimi.ingsw.model.cards.utils.Planet;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.state.VoyageState;

/**
 * Represents an "Abandoned Station" card game.
 * <p>
 * This card gives the player the chance to land on an abandoned station to retrieve goods.
 * Landing requires a minimal amount of crew and wastes travel days.
 * </p>
 */
public class AbandonedStationCard extends Card {

	private final Planet planet;
	private final int crew;

	public AbandonedStationCard(int id, int days, Planet planet, int crew) {
		super(id, days);
		if (crew <= 0) throw new NegativeArgumentException("Crew required can't be negative.");
		if (planet == null) throw new NullPointerException();
		this.crew = crew;
		this.planet = planet;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CardState getState(VoyageState state) {
		return new AbandonedStationAnnounceState(state, this, state.getOrder(CardOrder.NORMAL));
	}

	public Planet getPlanet() {
		return this.planet;
	}

	public int getCrewLost() {
		return this.crew;
	}

	/**
	 * <p><strong>To take advantage of this opportunity</strong>, the player must have at least the same
	 * number of Crew (Human + Alien) indicated on the map.</p>
	 *
	 * <p><strong>Only one player</strong> can take advantage of this opportunity.
	 * The leader decides first. If the leader does not have enough crew or does not want to miss flying days,
	 * the opportunity passes to the next player in order of course and so on.
	 * When someone decides to dock, the others are cut off.</p>
	 *
	 * @param state The Current State
	 * @param p The Player
	 * @param id The Id
	 * @throws CrewSizeException    if the player has insufficient crew
	 * @throws NullPointerException    if {@code state} or {@code p} is null
	 */
	public void apply(VoyageState state, Player p, int id) {
		if (state == null || p == null) throw new NullPointerException();
		if (id == 0) {
			if (p.getSpaceShip().getTotalCrew() < this.crew)
				throw new CrewSizeException("Crew too small to salvage station.");
			this.exhaust();
			state.getPlanche().movePlayer(state, p, -this.days);
		}
	}

}