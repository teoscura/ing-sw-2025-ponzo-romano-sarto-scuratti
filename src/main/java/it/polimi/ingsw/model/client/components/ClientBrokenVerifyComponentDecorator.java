package it.polimi.ingsw.model.client.components;

/**
 * Client side component decorator marking it as broken or invalid.
 */
public class ClientBrokenVerifyComponentDecorator implements ClientComponent {

	private final ClientComponent base;

	public ClientBrokenVerifyComponentDecorator(ClientComponent base) {
		if (base == null) throw new NullPointerException();
		this.base = base;
	}

	public ClientComponent getBase() {
		return this.base;
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
