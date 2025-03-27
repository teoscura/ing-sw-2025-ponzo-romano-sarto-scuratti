package it.polimi.ingsw.messages;

import it.polimi.ingsw.server.ClientDescriptor;
import it.polimi.ingsw.server.iServerController;

public abstract class ServerMessage implements Message{
    public abstract void recieve(ClientDescriptor client, iServerController controller);
}
