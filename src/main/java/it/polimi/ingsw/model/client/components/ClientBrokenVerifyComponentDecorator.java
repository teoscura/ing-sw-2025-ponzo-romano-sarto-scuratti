package it.polimi.ingsw.model.client.components;

import it.polimi.ingsw.view.ClientView;

public class ClientBrokenVerifyComponentDecorator implements ClientComponent {

	private final ClientComponent base;

	public ClientBrokenVerifyComponentDecorator(ClientComponent base) {
		if (base == null) throw new NullPointerException();
		this.base = base;
	}

	@Override
	public void showComponent(ClientView view) {
		base.showComponent(view);
		view.show(this);
	}

}
