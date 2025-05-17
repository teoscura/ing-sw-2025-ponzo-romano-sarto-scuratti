package it.polimi.ingsw.model.client.components;

import it.polimi.ingsw.model.components.enums.ShieldType;

public class ClientShieldComponentDecorator implements ClientComponent {

    private final ClientComponent base;
    private final ShieldType type;

	public ClientShieldComponentDecorator(ClientComponent base, ShieldType type) {
		if (base == null) throw new NullPointerException();
		this.base = base;
        this.type = type;
	}

    public ClientComponent getBase(){
		return this.base;
	}

    @Override
    public void showComponent(ClientComponentVisitor visitor) {
        base.showComponent(visitor);
        visitor.show(this);
    }

    public ShieldType getType(){
        return this.type;
    }

}
