package it.polimi.ingsw.model.client.components;

import it.polimi.ingsw.model.components.enums.AlienType;

public class ClientLifeSupportComponentDecorator implements ClientComponent {

	private final ClientComponent base;
	private final AlienType type;

	public ClientLifeSupportComponentDecorator(ClientComponent base, AlienType type) {
		if (base == null || !type.getLifeSupportExists()) throw new NullPointerException();
		this.base = base;
		this.type = type;
	}

	public ClientComponent getBase(){
		return this.base;
	}

	public AlienType getAlienType() {
		return this.type;
	}

	@Override
	public void showComponent(ClientComponentVisitor visitor) {
		base.showComponent(visitor);
		visitor.show(this);
	}
}