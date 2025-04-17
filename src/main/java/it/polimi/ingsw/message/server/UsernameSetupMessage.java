package it.polimi.ingsw.message.server;

import it.polimi.ingsw.controller.server.ServerController;
import it.polimi.ingsw.model.adventure_cards.exceptions.ForbiddenCallException;

public class UsernameSetupMessage extends ServerMessage{

    private final String username;

    public UsernameSetupMessage(String username){
        if(username == null) throw new NullPointerException();
        this.username = username;
    }

    @Override
    public void receive(ServerController server) throws ForbiddenCallException {
        x;
    }
    
}
