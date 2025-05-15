package it.polimi.ingsw.model.client.card;

import it.polimi.ingsw.view.ClientView;

public class ClientCombatZoneIndexCardStateDecorator implements ClientCardState {

	private final ClientCardState base;

	private final int index;

	public ClientCombatZoneIndexCardStateDecorator(ClientCardState base, int index) {
		if (base == null || index < 0 || index > 3) throw new NullPointerException();
		this.base = base;
		this.index = index;
	}

	public int getIndex() {
		return this.index;
	}

	@Override
	public void showCardState(ClientView view) {
		base.showCardState(view);
		view.show(this);
	}

}
