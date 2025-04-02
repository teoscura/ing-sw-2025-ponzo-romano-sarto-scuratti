package it.polimi.ingsw.message.server;

import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.controller.server.iServerController;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.adventure_cards.state.CardState;
import it.polimi.ingsw.model.state.GameState;

public abstract class ServerMessage {

    private final ClientDescriptor descriptor;

    protected ServerMessage(ClientDescriptor descriptor){
        if(descriptor==null) throw new NullPointerException();
        this.descriptor = descriptor;
    }

    public ClientDescriptor getDescriptor(){
        return this.descriptor;
    }

    public abstract void receive(iServerController server);
    public abstract void receive(ModelInstance instance);
    public abstract void receive(GameState state);
    public abstract void receive(CardState state);
}
