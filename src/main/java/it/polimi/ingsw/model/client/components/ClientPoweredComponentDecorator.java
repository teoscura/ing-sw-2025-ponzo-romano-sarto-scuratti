package it.polimi.ingsw.model.client.components;

/**
 * Client side component decorator containing information about its powered state.
 */
public class ClientPoweredComponentDecorator implements ClientComponent {

	private final ClientComponent base;

	private final boolean powered;

	public ClientPoweredComponentDecorator(ClientComponent base, boolean powered) {
		if (base == null) throw new NullPointerException();
		this.base = base;
		this.powered = powered;
	}

	public ClientComponent getBase() {
		return this.base;
	}

	public boolean getPowered() {
		return this.powered;
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
