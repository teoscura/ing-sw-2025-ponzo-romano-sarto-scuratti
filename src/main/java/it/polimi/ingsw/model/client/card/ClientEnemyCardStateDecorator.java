package it.polimi.ingsw.model.client.card;

import it.polimi.ingsw.model.cards.utils.CombatZonePenalty;

/**
 * Client side card state decorator displaying an enemy info, power, and penalty in case of loss against it.
 */
public class ClientEnemyCardStateDecorator implements ClientCardState {

	private final ClientCardState base;

	private final double power;
	private final CombatZonePenalty penalty;
	private final int amount;

	public ClientEnemyCardStateDecorator(ClientCardState base, double power, CombatZonePenalty penalty, int amount) {
		if (base == null) throw new NullPointerException();
		if (penalty == CombatZonePenalty.DAYS) throw new IllegalArgumentException();
		this.base = base;
		this.power = power;
		this.penalty = penalty;
		this.amount = amount;
	}

	public double getPower() {
		return this.power;
	}

	public CombatZonePenalty getPenalty() {
		return this.penalty;
	}

	public int getAmount() {
		return this.amount;
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
