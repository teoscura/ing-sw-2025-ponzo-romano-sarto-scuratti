package it.polimi.ingsw.model.client.components;

import it.polimi.ingsw.model.components.enums.StorageType;

/**
 * Client side component decorator containing info about the cargo it contains.
 */
public class ClientShipmentsComponentDecorator implements ClientComponent {

	private final ClientComponent base;
	private final StorageType type;
	private final int[] shipments;

	public ClientShipmentsComponentDecorator(ClientComponent base, StorageType type, int[] shipments) {
		if (base == null || shipments == null || shipments.length != 4) throw new NullPointerException();
		this.type = type;
		this.base = base;
		this.shipments = shipments;
	}

	public ClientComponent getBase() {
		return this.base;
	}

	public StorageType getType() {
		return this.type;
	}

	public int[] getShipments() {
		return this.shipments;
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
