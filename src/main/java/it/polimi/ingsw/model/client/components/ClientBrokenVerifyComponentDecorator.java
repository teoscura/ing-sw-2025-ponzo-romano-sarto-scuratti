package it.polimi.ingsw.model.client.components;

public class ClientBrokenVerifyComponentDecorator implements ClientComponent {

	private final ClientComponent base;

	public ClientBrokenVerifyComponentDecorator(ClientComponent base) {
		if (base == null) throw new NullPointerException();
		this.base = base;
	}

	public ClientComponent getBase(){
		return this.base;
	}

	@Override
	public void showComponent(ClientComponentVisitor visitor) {
		base.showComponent(visitor);
		visitor.show(this);
	}

}
