//Done.
package it.polimi.ingsw.model.cards;

import java.util.ArrayList;

import it.polimi.ingsw.exceptions.NegativeArgumentException;
import it.polimi.ingsw.model.cards.state.CardState;
import it.polimi.ingsw.model.cards.state.SmugglersAnnounceState;
import it.polimi.ingsw.model.cards.utils.*;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.state.VoyageState;

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

	@Override
	public CardState getState(VoyageState state) {
		return new SmugglersAnnounceState(state, this, new ArrayList<>(state.getOrder(CardOrder.NORMAL)));
	}

	public Planet getReward() {
		return this.reward;
	}

	public int getCargoPenalty() {
		return this.cargo_taken;
	}

	public boolean apply(Player p) {
		if (p == null) throw new NullPointerException();
		if (p.getSpaceShip().getCannonPower() > this.min_power) {
			System.out.println("Player '"+p.getUsername()+"' beat the smugglers!");
			this.exhaust();
			return true;
		} else if (p.getSpaceShip().getCannonPower() == this.min_power){
			System.out.println("Player '"+p.getUsername()+"' tied the smugglers!");
			return true;
		}
		System.out.println("Player '"+p.getUsername()+"' lost to the smugglers!");
		return p.getSpaceShip().getCannonPower() == this.min_power;
	}

}
