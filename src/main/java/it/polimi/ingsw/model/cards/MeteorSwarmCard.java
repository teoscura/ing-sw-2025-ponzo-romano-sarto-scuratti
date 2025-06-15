package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.state.CardState;
import it.polimi.ingsw.model.cards.state.MeteorAnnounceState;
import it.polimi.ingsw.model.cards.utils.Projectile;
import it.polimi.ingsw.model.cards.utils.ProjectileArray;
import it.polimi.ingsw.model.state.VoyageState;

import java.util.ArrayList;

/**
 * Represents the "Meteor Swarm" adventure card in the game.
 * <p>
 * This card simulates a meteor shower where a specific meteor
 * hits the player's ship. The card interacts with the ship's defenses and can lead
 * to structural damage or destruction of key ship parts, including the central cabin.
 * </p>
 */
public class MeteorSwarmCard extends Card {

	private final ProjectileArray meteorites;

	public MeteorSwarmCard(int id, ProjectileArray meteorites) {
		super(id, 0);
		this.meteorites = meteorites;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CardState getState(VoyageState state) {
		return new MeteorAnnounceState(state, this.getId(), meteorites);
	}

	public ArrayList<Projectile> getMeteorites() {
		return this.meteorites.getProjectiles();
	}

}

