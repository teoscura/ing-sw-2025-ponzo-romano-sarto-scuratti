package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.exceptions.NegativeArgumentException;
import it.polimi.ingsw.model.cards.state.CardState;
import it.polimi.ingsw.model.cards.state.SlaversAnnounceState;
import it.polimi.ingsw.model.cards.utils.CardOrder;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.state.VoyageState;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.LoggerLevel;

import java.util.ArrayList;

/**
 * Represents the "Slavers" adventure card in the game.
 * <p>
 * This card challenges players based on their cannon power. If the player
 * has sufficient firepower, they gain a reward. If not, they may suffer a
 * crew loss or even lose the game if their crew is too small.
 * </p>
 */
public class SlaversCard extends Card {

	private final double min_power;
	private final int crew_penalty;
	private final int credits;

	public SlaversCard(int id, int days, double min_power, int crew_penalty, int credits) {
		super(id, days);
		if (credits <= 0 || crew_penalty <= 0) throw new NegativeArgumentException();
		this.min_power = min_power;
		this.crew_penalty = crew_penalty;
		this.credits = credits;
	}

	@Override
	public CardState getState(VoyageState state) {
		return new SlaversAnnounceState(state, this, new ArrayList<>(state.getOrder(CardOrder.NORMAL)));
	}

	public double getPower() {
		return this.min_power;
	}

	public int getCredits() {
		return credits;
	}

	public int getCrewLost() {
		return this.crew_penalty;
	}

	/**
	 * Applies the effect of the Slavers card based on the ship's cannon power.
	 * <p>
	 * - If cannon power is greater than {@code min_power}, the player wins and gains credits.<br>
	 * - If equal, nothing happens.<br>
	 * - If less, the player loses crew or the game.
	 * </p>
	 *
	 * @param state The Current State
	 * @param p The Player
	 * @throws NullPointerException    if {@code state} or {@code p} is null
	 */
	public boolean apply(VoyageState state, Player p) {
		if (state == null || p == null) throw new NullPointerException();
		if (p.getSpaceShip().getCannonPower() > this.min_power) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' beat the slavers!");
			this.exhaust();
			return true;
		} else if (p.getSpaceShip().getCannonPower() == this.min_power) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' tied the slavers!");
			return true;
		}
		if (p.getSpaceShip().getTotalCrew() <= this.crew_penalty) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' lost and the whole crew got captured by slavers!");
			state.loseGame(p);
			return false;
		}
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' lost to the slavers!");
		return false;
	}

}

