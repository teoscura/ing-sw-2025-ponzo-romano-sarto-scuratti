package it.polimi.ingsw.model.client.components;

import it.polimi.ingsw.view.ClientView;

public class ClientPoweredComponentDecorator implements ClientComponent {

    private final ClientComponent base;

    private final boolean powered;

    public ClientPoweredComponentDecorator(ClientComponent base, boolean powered) {
        if(base == null) throw new NullPointerException();
        this.base = base;
        this.powered = powered;
    }

    public boolean getPowered(){
        return this.powered;
    }

    @Override
    public void showComponent(ClientView view) {
        base.showComponent(view);
        view.show(this);
    }

}
