package it.polimi.ingsw.message.client;

import it.polimi.ingsw.controller.client.ClientController;
import it.polimi.ingsw.model.client.state.ClientState;

public class NotifyStateUpdateMessage extends ClientMessage {

    private final ClientState state;

    public NotifyStateUpdateMessage(ClientState state){
        if(state == null) throw new NullPointerException();
        this.state = state;
    }

    @Override
    public void receive(ClientController client) {
        this.state.sendToView(client.getView());
    }
    
}
