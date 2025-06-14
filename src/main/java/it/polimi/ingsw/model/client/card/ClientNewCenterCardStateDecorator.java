package it.polimi.ingsw.model.client.card;

import it.polimi.ingsw.model.player.PlayerColor;

import java.util.ArrayList;

/**
 * Client side card state decorator displaying the list of players the model is awaiting the selection of a ship part from.
 */
public class ClientNewCenterCardStateDecorator implements ClientCardState {

	private final ClientCardState base;

	private final ArrayList<PlayerColor> awaiting;

	public ClientNewCenterCardStateDecorator(ClientCardState base, ArrayList<PlayerColor> awaiting) {
		if (base == null || awaiting == null) throw new NullPointerException();
		this.base = base;
		this.awaiting = awaiting;
	}

	public ArrayList<PlayerColor> getAwaiting() {
		return awaiting;
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