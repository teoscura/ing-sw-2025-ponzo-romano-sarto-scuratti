package it.polimi.ingsw.model.client.components;

public class ClientShipmentsComponentDecorator implements ClientComponent {

	private final ClientComponent base;
	private final int[] shipments;

	public ClientShipmentsComponentDecorator(ClientComponent base, int[] shipments) {
		if (base == null || shipments == null || shipments.length != 4) throw new NullPointerException();
		this.base = base;
		this.shipments = shipments;
	}

	public ClientComponent getBase(){
		return this.base;
	}

	public int[] getShipments() {
		return this.shipments;
	}

	@Override
	public void showComponent(ClientComponentVisitor visitor) {
		base.showComponent(visitor);
		visitor.show(this);
	}

}
