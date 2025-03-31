package it.polimi.ingsw.message.server;

public abstract class ServerMessage {
    public abstract void recieve(ClientDescriptor client, iServerController server);
}
