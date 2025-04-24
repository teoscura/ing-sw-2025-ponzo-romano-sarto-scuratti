//Done.
package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.model.adventure_cards.state.AbandonedShipAnnounceState;
import it.polimi.ingsw.model.adventure_cards.state.CardState;
import it.polimi.ingsw.model.adventure_cards.utils.CardOrder;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.state.VoyageState;

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
		return this.getCrewLost();
	}

	public void apply(VoyageState state, Player p, int id) {
		if (state == null || p == null) throw new NullPointerException();
		if (id == 0) {
			if (p.getSpaceShip().getTotalCrew() <= this.crew_lost)
				throw new IllegalArgumentException("The crew isn't big enough for this abandoned ship.");
			this.exhaust();
		}
	}

}