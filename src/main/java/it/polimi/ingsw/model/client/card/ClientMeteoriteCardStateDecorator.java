package it.polimi.ingsw.model.client.card;

import it.polimi.ingsw.model.cards.utils.Projectile;

/**
 * Client side card state decorator displaying an incoming meteorite, its direction and size.
 */
public class ClientMeteoriteCardStateDecorator implements ClientCardState {

	private final ClientCardState base;

	private final Projectile meteorite;

	public ClientMeteoriteCardStateDecorator(ClientCardState base, Projectile projectile) {
		if (base == null || projectile == null || projectile.getOffset() < 0) throw new NullPointerException();
		this.base = base;
		this.meteorite = projectile;
	}

	public Projectile getProjectile() {
		return this.meteorite;
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
