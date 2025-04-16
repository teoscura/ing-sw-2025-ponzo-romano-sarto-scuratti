package it.polimi.ingsw.message.client;

import it.polimi.ingsw.controller.client.iClientController;
import it.polimi.ingsw.model.client.state.ClientModelState;

public class NotifyStateUpdateMessage extends ClientMessage {

    private final ClientModelState state;

    public NotifyStateUpdateMessage(ClientModelState state){
        if(state == null) throw new NullPointerException();
        this.state = state;
    }

    @Override
    public void receive(iClientController client) {
        this.state.sendToView(client.getView());
    }
    
}
