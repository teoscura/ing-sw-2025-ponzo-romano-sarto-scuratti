package it.polimi.ingsw.message.server;

import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.controller.server.ServerController;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.adventure_cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.adventure_cards.state.CardState;
import it.polimi.ingsw.model.state.GameState;

public class DisconnectMessage extends ServerMessage {

    protected DisconnectMessage(ClientDescriptor descriptor) {
        super(descriptor);
        if(this.descriptor.getPlayer()==null) throw new NullPointerException();
    }

    @Override
    public void receive(ServerController server) throws ForbiddenCallException {
        server.getModel().validate(this);
    }

    @Override
    public void receive(ModelInstance instance) throws ForbiddenCallException {
        instance.getState().validate(this);
    }

    @Override
    public void receive(GameState state) throws ForbiddenCallException {
        state.getCardState(this.descriptor.getPlayer()).validate(this);
    }

    @Override
    public void receive(CardState state) throws ForbiddenCallException {
        state.disconnect(this.descriptor.getPlayer());
    }
    
}
