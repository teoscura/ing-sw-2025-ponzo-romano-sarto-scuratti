package it.polimi.ingsw.model.client.card;

import it.polimi.ingsw.model.cards.utils.Planet;
import it.polimi.ingsw.model.player.PlayerColor;

import java.util.ArrayList;

public class ClientLandingCardStateDecorator implements ClientCardState {

	private final ClientCardState base;

	private final PlayerColor turn;
	private final int days_taken;
	private final int crew_needed;
	private final int credits;
	private final ArrayList<Planet> available;

	public ClientLandingCardStateDecorator(ClientCardState base, PlayerColor turn, int days_taken, int crew_needed, int credits, ArrayList<Planet> available) {
		if (base == null || available == null) throw new NullPointerException();
		if (days_taken <= 0 || crew_needed < 0) throw new IllegalArgumentException();
		this.base = base;
		this.turn = turn;
		this.days_taken = days_taken;
		this.crew_needed = crew_needed;
		this.credits = credits;
		this.available = available;
	}

	public PlayerColor getTurn() {
		return this.turn;
	}

	public int getDaysTaken() {
		return this.days_taken;
	}

	public int getCrewNeeded() {
		return this.crew_needed;
	}

	public int getCredits(){
		return this.credits;
	}

	public ArrayList<Planet> getAvailable() {
		return this.available;
	}

	@Override
	public void showCardState(ClientCardStateVisitor visitor) {
		base.showCardState(visitor);
		visitor.show(this);
	}

}

