package it.polimi.ingsw.model.client.card;

import it.polimi.ingsw.model.cards.utils.CombatZoneSection;

/**
 * Client side card state decorator displaying the index and section info of a combat zone card.
 */
public class ClientCombatZoneIndexCardStateDecorator implements ClientCardState {

	private final ClientCardState base;

	private final CombatZoneSection section;

	private final int index;

	public ClientCombatZoneIndexCardStateDecorator(ClientCardState base, CombatZoneSection section, int index) {
		if (base == null || section == null || index < 0 || index > 3) throw new NullPointerException();
		this.base = base;
		this.section = section;
		this.index = index;
	}

	public CombatZoneSection getSection() {
		return this.section;
	}

	public int getIndex() {
		return this.index;
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
