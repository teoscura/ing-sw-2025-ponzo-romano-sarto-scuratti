package it.polimi.ingsw.model.client.card;

import it.polimi.ingsw.model.cards.utils.Projectile;

public class ClientProjectileCardStateDecorator implements ClientCardState {

	private final ClientCardState base;

	private final Projectile shot;

	public ClientProjectileCardStateDecorator(ClientCardState base, Projectile projectile) {
		if (base == null || projectile == null || projectile.getOffset() < 0) throw new NullPointerException();
		this.base = base;
		this.shot = projectile;
	}

	public Projectile getProjectile() {
		return this.shot;
	}

	@Override
	public void showCardState(ClientCardStateVisitor visitor) {
		base.showCardState(visitor);
		visitor.show(this);
	}
}
