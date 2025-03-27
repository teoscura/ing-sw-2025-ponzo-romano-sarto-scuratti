package it.polimi.ingsw.controller.message.server;

import it.polimi.ingsw.controller.message.iServerMessageHandler;

public class UserDisconnectMessage extends ServerMessage {
    
    @Override
    public void sendTo(iServerMessageHandler m) {
        m.onMessage(this);
    }

    //TODO.
    
}
