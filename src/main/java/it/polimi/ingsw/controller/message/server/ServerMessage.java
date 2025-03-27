package it.polimi.ingsw.controller.message.server;

import it.polimi.ingsw.controller.message.Message;
import it.polimi.ingsw.controller.message.iGameMessageHandler;
import it.polimi.ingsw.controller.message.iServerMessageHandler;

public abstract class ServerMessage implements Message {

    @Override
    public void sendTo(iGameMessageHandler m) throws Exception{
        throw new Exception("A mistake happened in message handling! must've sent it somewhere wrong!");
    }

    
    public abstract void sendTo(iServerMessageHandler m);
    
}
