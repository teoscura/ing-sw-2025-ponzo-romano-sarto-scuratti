package it.polimi.ingsw.messages.client;

import it.polimi.ingsw.controller.client.iClientController;
import it.polimi.ingsw.messages.ClientMessage;

public class PingMessage extends ClientMessage{

    @Override
    public void recieve(iClientController client) {
        client.ping();
    }
    
}
