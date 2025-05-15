package it.polimi.ingsw.model.client.components;

import it.polimi.ingsw.view.ClientView;

public class ClientBatteryComponentDecorator implements ClientComponent {

	private final ClientComponent base;
	private final int batteries;

	public ClientBatteryComponentDecorator(ClientComponent base, int batteries) {
		if (base == null || batteries < 0 || batteries > 3) throw new NullPointerException();
		this.base = base;
		this.batteries = batteries;
	}

	public int getBatteries() {
		return this.batteries;
	}

	@Override
	public void showComponent(ClientView view) {
		base.showComponent(view);
		view.show(this);
	}
}
