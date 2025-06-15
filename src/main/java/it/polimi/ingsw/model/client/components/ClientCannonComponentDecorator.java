package it.polimi.ingsw.model.client.components;

import it.polimi.ingsw.model.components.enums.ComponentRotation;

/**
 * Client side component decorator containing info about its cannon's power and direction.
 */
public class ClientCannonComponentDecorator implements ClientComponent {

	private final ClientComponent base;

	private final ComponentRotation rotation;

	public ClientCannonComponentDecorator(ClientComponent base, ComponentRotation rotation) {
		if (base == null) throw new NullPointerException();
		this.base = base;
		this.rotation = rotation;
	}

	public ClientComponent getBase() {
		return this.base;
	}

	public ComponentRotation getRotation() {
		return this.rotation;
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
