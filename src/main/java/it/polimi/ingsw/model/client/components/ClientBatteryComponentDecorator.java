package it.polimi.ingsw.model.client.components;

/**
 * Client side component decorator containing info about its battery contents.
 */
public class ClientBatteryComponentDecorator implements ClientComponent {

	private final ClientComponent base;
	private final int batteries;

	public ClientBatteryComponentDecorator(ClientComponent base, int batteries) {
		if (base == null || batteries < 0 || batteries > 3) throw new NullPointerException();
		this.base = base;
		this.batteries = batteries;
	}

	public ClientComponent getBase() {
		return this.base;
	}

	public int getBatteries() {
		return this.batteries;
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
