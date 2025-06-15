//Done.
package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.exceptions.ArgumentTooBigException;
import it.polimi.ingsw.model.cards.exceptions.AlreadyVisitedException;
import it.polimi.ingsw.model.cards.state.CardState;
import it.polimi.ingsw.model.cards.state.PlanetAnnounceState;
import it.polimi.ingsw.model.cards.utils.CardOrder;
import it.polimi.ingsw.model.cards.utils.Planet;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.state.VoyageState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * Represents a "Planet" adventure card in the game
 * this card allows players to land on available planets and collect cargo.
 * <p>
 * The player receives a list of available {@link Planet} options and selects one to land on.
 * If the selected planet has not been visited, the player collects its cargo and loses a number of days.
 * The card is exhausted once all planets have been visited or skipped.
 * </p>
 */
public class PlanetCard extends Card {

	private final ArrayList<Planet> planets;
	private int left;

	public PlanetCard(int id, int days, Planet[] planets) { // costruttore
		super(id, days);
		this.planets = new ArrayList<>(Arrays.asList(planets));
		this.left = planets.length;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CardState getState(VoyageState state) {
		return new PlanetAnnounceState(state, this, new ArrayList<>(state.getOrder(CardOrder.NORMAL)));
	}

	public List<Boolean> getVisited() {
		return this.planets.stream().map(p -> p.getVisited()).toList();
	}

	public Planet getPlanet(int id) {
		return this.planets.get(id);
	}

	public int getSize() {
		return planets.size();
	}

	public ArrayList<Planet> getPlanets() {
		return new ArrayList<>(this.planets);
	}

	/**
	 * Applies the landing effect of this card for the given spaceship and player.
	 * <p>
	 * If the planet selected has already been visited, or the response is -1 (skip), nothing happens.
	 * If a valid and unvisited planet is chosen, it is marked visited, the player receives cargo,
	 * and the card is exhausted once all planets are resolved.
	 * </p>
	 *
	 * @param p The Player
	 * @param id The Id
	 * @throws NullPointerException        if {@code p} is null
	 * @throws ArgumentTooBigException     if the selected planet index is out of bounds
	 */
	public void apply(Player p, int id) {
		if (p == null) throw new NullPointerException();
		if (id >= this.planets.size()) throw new ArgumentTooBigException("Sent a planet id larger than the list.");
		if (id == -1) {
			return;
		}
		if (this.planets.get(id).getVisited()) throw new AlreadyVisitedException();
		this.left--;
		this.planets.get(id).visit();
		if (left == 0) this.exhaust();
	}

}