package it.polimi.ingsw.controller.message.player;

import it.polimi.ingsw.controller.message.Message;
import it.polimi.ingsw.controller.message.iGameMessageHandler;
import it.polimi.ingsw.controller.message.iServerMessageHandler;

public abstract class PlayerMessage implements Message {

    public abstract void sendTo(iGameMessageHandler m);

    @Override
    public void sendTo(iServerMessageHandler m) throws Exception {
        throw new Exception("A mistake happened in message handling! must've sent it somewhere wrong!");
    }

    //TODO.
    
}
