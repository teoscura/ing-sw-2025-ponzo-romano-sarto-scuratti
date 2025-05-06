package it.polimi.ingsw.controller.client.state;

import it.polimi.ingsw.controller.client.ClientController;
import it.polimi.ingsw.view.ClientView;

public abstract class ClientControllerState {
    
    protected final ClientController controller;
    protected final ClientView view;

    public ClientControllerState(ClientController controller, ClientView view){
        if(view == null || controller == null) throw new NullPointerException();
        this.controller = controller;
        this.view = view;
    }

    public abstract void init();
    public abstract ClientControllerState getNext();
    public abstract void nextStep();

    protected void transition(){
        this.controller.setState(this.getNext());
    }

    protected ClientView getView(){
        return this.view;
    }

}
