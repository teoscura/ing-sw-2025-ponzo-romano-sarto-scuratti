package it.polimi.ingsw.model.client.card;

import it.polimi.ingsw.model.cards.utils.Projectile;
import it.polimi.ingsw.view.ClientView;

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
	public void showCardState(ClientView view) {
		base.showCardState(view);
		view.show(this);
	}
}
