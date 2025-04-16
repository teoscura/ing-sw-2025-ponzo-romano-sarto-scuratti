package it.polimi.ingsw.message.server;

import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.controller.server.ServerController;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.adventure_cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.adventure_cards.state.CardState;
import it.polimi.ingsw.model.state.GameState;

public class PingMessage extends ServerMessage {

    @Override
    public void receive(ServerController server) throws ForbiddenCallException {
        server.ping(this.descriptor);
    }
    
}
