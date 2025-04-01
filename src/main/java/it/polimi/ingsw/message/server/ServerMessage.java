package it.polimi.ingsw.message.server;

import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.controller.server.iServerController;

public abstract class ServerMessage {

    private final ClientDescriptor descriptor;

    protected ServerMessage(ClientDescriptor descriptor){
        if(descriptor==null) throw new NullPointerException();
        this.descriptor = descriptor;
    }

    public ClientDescriptor getDescriptor(){
        return this.descriptor;
    }
    public abstract void recieve(iServerController server);
}
