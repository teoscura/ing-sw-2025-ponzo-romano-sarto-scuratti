package it.polimi.ingsw.message.server;

import it.polimi.ingsw.controller.server.ServerController;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.state.GameState;

public class TakeComponentMessage extends ServerMessage {
    
    @Override
    public void receive(ServerController server) throws ForbiddenCallException {
        if(this.descriptor.getPlayer()==null) throw new ForbiddenCallException("Descriptor associated to message isn't bound to player");
        server.getModel().validate(this);
    }

    @Override
    public void receive(ModelInstance instance) throws ForbiddenCallException {
        instance.getState().validate(this);
    }

    @Override
    public void receive(GameState state) throws ForbiddenCallException {
        state.takeComponent(this.descriptor.getPlayer());
    }
    
}
