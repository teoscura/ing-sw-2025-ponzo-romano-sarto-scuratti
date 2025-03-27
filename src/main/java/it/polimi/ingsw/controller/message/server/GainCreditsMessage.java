package it.polimi.ingsw.controller.message.server;

import it.polimi.ingsw.controller.message.iGameMessageHandler;
import it.polimi.ingsw.controller.message.iServerMessageHandler;

public class GainCreditsMessage extends ServerMessage {
    
    @Override
    public void sendTo(iServerMessageHandler m) {
        m.onMessage(this);
    }
    
    //TODO.
    
}
