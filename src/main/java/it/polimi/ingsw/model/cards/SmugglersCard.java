//Done.
package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.exceptions.NegativeArgumentException;
import it.polimi.ingsw.model.cards.state.CardState;
import it.polimi.ingsw.model.cards.state.SmugglersAnnounceState;
import it.polimi.ingsw.model.cards.utils.CardOrder;
import it.polimi.ingsw.model.cards.utils.Planet;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.state.VoyageState;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.LoggerLevel;

import java.util.ArrayList;

/**
 * Represents the "Smuggler" card in the game.
 * <p>
 * Smugglers and other enemies are a
 * threat to everyone, but attack ships
 * of players in order of course. First
 * attack the leader. If they win, they attack the
 * next player, and so on, until
 * have attacked them all or until someone has attacked them
 * Defeats.
 * </p>
 */
public class SmugglersCard extends Card {

	private final Planet reward;
	private final int cargo_taken;
	private final double min_power;

	public SmugglersCard(int id, int days, Planet reward, int cargo_taken, double min_power) {
		super(id, days);
		if (reward == null) throw new NullPointerException();
		if (min_power <= 0 || cargo_taken <= 0) throw new NegativeArgumentException();
		this.reward = reward;
		this.cargo_taken = cargo_taken;
		this.min_power = min_power;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CardState getState(VoyageState state) {
		return new SmugglersAnnounceState(state, this, new ArrayList<>(state.getOrder(CardOrder.NORMAL)));
	}

	public double getPower() {
		return this.min_power;
	}

	public Planet getReward() {
		return this.reward;
	}

	public int getCargoPenalty() {
		return this.cargo_taken;
	}

	/**
	 * Apply card effects to the player:
	 * <ul>
	 * <li>If the power is sufficient: the player can claim the reward by losing flight days.</li>
	 * <li>If it's the same: nothing happens to the player, but the
	 * enemy is not defeated and goes ahead to attack the player
	 * next on the route.</li>
	 * <li>If inferior: The ship takes a series of hits. the player pay the penalty indicated in the top corner
	 * to the right of the map. After that the enemy attacks the player
	 * next on the route. If the middle substation is destroyed,
	 * the player is considered out of play.</li>
	 * </ul>
	 *
	 * @param state The Current State
	 * @param p The Player
	 * @throws NullPointerException if {@code p} is null
	 */
	public boolean apply(VoyageState state, Player p) {
		if (p == null) throw new NullPointerException();
		if (p.getSpaceShip().getCannonPower() > this.min_power) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' beat the smugglers!");
			this.exhaust();
			return true;
		} else if (p.getSpaceShip().getCannonPower() == this.min_power) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' tied the smugglers!");
			return true;
		}
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' lost to the smugglers!");
		return p.getSpaceShip().getCannonPower() == this.min_power;
	}

}
