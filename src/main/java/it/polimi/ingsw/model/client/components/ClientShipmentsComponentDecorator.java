package it.polimi.ingsw.model.client.components;

import it.polimi.ingsw.view.ClientView;

public class ClientShipmentsComponentDecorator implements ClientComponent {

	private final ClientComponent base;
	private final int[] shipments;

	public ClientShipmentsComponentDecorator(ClientComponent base, int[] shipments) {
		if (base == null || shipments == null || shipments.length != 4) throw new NullPointerException();
		this.base = base;
		this.shipments = shipments;
	}

	public int[] getShipments() {
		return this.shipments;
	}

	@Override
	public void showComponent(ClientView view) {
		base.showComponent(view);
		view.show(this);
	}

}
