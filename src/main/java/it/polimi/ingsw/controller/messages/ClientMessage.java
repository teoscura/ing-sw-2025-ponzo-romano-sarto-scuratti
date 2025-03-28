package it.polimi.ingsw.controller.messages;

import it.polimi.ingsw.controller.client.iClientController;

public abstract class ClientMessage implements Message {
    public abstract void recieve(iClientController client);
}
