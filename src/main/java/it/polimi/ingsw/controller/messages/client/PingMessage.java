package it.polimi.ingsw.controller.messages.client;

import it.polimi.ingsw.controller.client.iClientController;
import it.polimi.ingsw.controller.messages.ClientMessage;

public class PingMessage extends ClientMessage{

    @Override
    public void recieve(iClientController client) {
        client.ping();
    }
    
}
