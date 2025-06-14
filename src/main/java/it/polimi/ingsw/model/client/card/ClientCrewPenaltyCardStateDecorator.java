package it.polimi.ingsw.model.client.card;

import it.polimi.ingsw.model.player.PlayerColor;

/**
 * Client side card state decorator displaying a crew penalty applied to a specified player.
 */
public class ClientCrewPenaltyCardStateDecorator implements ClientCardState {

	private final ClientCardState base;

	private final PlayerColor turn;
	private final int crew_amount;

	public ClientCrewPenaltyCardStateDecorator(ClientCardState base, PlayerColor turn, int crew_amount) {
		if (base == null || crew_amount < 0 || turn == PlayerColor.NONE) throw new NullPointerException();
		this.base = base;
		this.turn = turn;
		this.crew_amount = crew_amount;
	}

	public PlayerColor getTurn() {
		return this.turn;
	}

	public int getCrewLost() {
		return this.crew_amount;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showCardState(ClientCardStateVisitor visitor) {
		base.showCardState(visitor);
		visitor.show(this);
	}

}
