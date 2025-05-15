package it.polimi.ingsw.model.client.card;

import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.view.ClientView;

public class ClientCreditsRewardCardStateDecorator implements ClientCardState {

	private final ClientCardState base;

	private final PlayerColor turn;
	private final int credits;
	private final int days_taken;

	public ClientCreditsRewardCardStateDecorator(ClientCardState base, PlayerColor turn, int credits, int days_taken) {
		if (base == null || credits < 0 || days_taken < 0 || turn == PlayerColor.NONE) throw new NullPointerException();
		this.base = base;
		this.turn = turn;
		this.credits = credits;
		this.days_taken = days_taken;
	}

	public PlayerColor getTurn() {
		return this.turn;
	}

	public int getCredits() {
		return this.credits;
	}

	public int getDaysTaken() {
		return this.days_taken;
	}

	@Override
	public void showCardState(ClientView view) {
		base.showCardState(view);
		view.show(this);
	}

}
