package it.polimi.ingsw.model.client.card;

import it.polimi.ingsw.model.player.PlayerColor;

public class ClientCargoPenaltyCardStateDecorator implements ClientCardState {

	private final ClientCardState base;

	private final PlayerColor turn;
	private final int[] shipments;

	public ClientCargoPenaltyCardStateDecorator(ClientCardState base, PlayerColor turn, int[] shipments) {
		if (base == null || shipments == null || shipments.length != 5 || turn == PlayerColor.NONE)
			throw new NullPointerException();
		for (int t : shipments) {
			if (t < 0) throw new IllegalArgumentException();
		}
		this.base = base;
		this.turn = turn;
		this.shipments = shipments;
	}

	public PlayerColor getTurn() {
		return this.turn;
	}

	public int[] getShipments() {
		return this.shipments;
	}

	@Override
	public void showCardState(ClientCardStateVisitor visitor) {
		base.showCardState(visitor);
		visitor.show(this);
	}

}
