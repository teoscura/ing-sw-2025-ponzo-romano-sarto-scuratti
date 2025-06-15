package it.polimi.ingsw.model.client.components;

import it.polimi.ingsw.model.components.enums.ShieldType;

/**
 * Client side component decorator containing info about the directions it is shielding.
 */
public class ClientShieldComponentDecorator implements ClientComponent {

	private final ClientComponent base;
	private final ShieldType type;

	public ClientShieldComponentDecorator(ClientComponent base, ShieldType type) {
		if (base == null) throw new NullPointerException();
		this.base = base;
		this.type = type;
	}

	public ClientComponent getBase() {
		return this.base;
	}

	public ShieldType getType() {
		return this.type;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showComponent(ClientComponentVisitor visitor) {
		base.showComponent(visitor);
		visitor.show(this);
	}

}
