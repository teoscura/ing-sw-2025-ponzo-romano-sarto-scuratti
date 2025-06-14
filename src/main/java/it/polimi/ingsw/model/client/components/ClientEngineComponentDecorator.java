package it.polimi.ingsw.model.client.components;

import it.polimi.ingsw.model.components.enums.ComponentRotation;

/**
 * Client side component decorator containing info about its engine's power and direction.
 */
public class ClientEngineComponentDecorator implements ClientComponent {

	private final ClientComponent base;

	private final ComponentRotation rotation;

	public ClientEngineComponentDecorator(ClientComponent base, ComponentRotation rotation) {
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