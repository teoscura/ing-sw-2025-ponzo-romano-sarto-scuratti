package it.polimi.ingsw.messages;

import it.polimi.ingsw.server.ClientDescriptor;
import it.polimi.ingsw.server.iServersideController;

public abstract class ServerMessage implements Message{
    public abstract void recieve(ClientDescriptor client, iServersideController controller);
}
