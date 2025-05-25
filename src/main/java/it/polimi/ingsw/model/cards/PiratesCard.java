//Done.
package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.exceptions.NegativeArgumentException;
import it.polimi.ingsw.model.cards.state.CardState;
import it.polimi.ingsw.model.cards.state.PiratesAnnounceState;
import it.polimi.ingsw.model.cards.utils.CardOrder;
import it.polimi.ingsw.model.cards.utils.Projectile;
import it.polimi.ingsw.model.cards.utils.ProjectileArray;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.state.VoyageState;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.LoggerLevel;

import java.util.ArrayList;


public class PiratesCard extends Card {

	private final ProjectileArray shots;
	private final int credits;
	private final double min_power;

	public PiratesCard(int id, int days, ProjectileArray shots, double min_power, int credits) {
		super(id, days);
		if (shots == null) throw new NullPointerException();
		if (min_power <= 0 || credits <= 0)
			throw new NegativeArgumentException("Pirate power/rewards cannot be less than one.");
		this.credits = credits;
		this.shots = shots;
		this.min_power = min_power;
	}

	@Override
	public CardState getState(VoyageState state) {
		return new PiratesAnnounceState(state, this, state.getOrder(CardOrder.NORMAL));
	}

	public int getCredits() {
		return this.credits;
	}

	public double getPower() {
		return this.min_power;
	}

	public ArrayList<Projectile> getShotsCopy() {
		return new ArrayList<>(this.shots.getProjectiles());
	}

	public ArrayList<Projectile> getShots() {
		return this.shots.getProjectiles();
	}

	public boolean apply(VoyageState state, Player p) {
		if (state == null || p == null) throw new NullPointerException();
		if (p.getSpaceShip().getCannonPower() > this.min_power) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' beat the pirates!");
			this.exhaust();
			return true;
		} else if (p.getSpaceShip().getCannonPower() == this.min_power) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' tied the pirates!");
			return true;
		}
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' lost to the pirates!");
		return p.getSpaceShip().getCannonPower() == this.min_power;
	}

}
