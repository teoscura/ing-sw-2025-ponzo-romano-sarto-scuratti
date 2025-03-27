package it.polimi.ingsw.controller.message.player;

import it.polimi.ingsw.controller.message.iGameMessageHandler;

public class PlayerContinueMessage extends PlayerMessage {
    
    @Override
    public void sendTo(iGameMessageHandler m) {
        m.onMessage(this);
    }

    //TODO.
    
}
