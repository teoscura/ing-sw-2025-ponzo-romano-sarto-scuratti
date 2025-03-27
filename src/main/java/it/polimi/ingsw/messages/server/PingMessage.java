package it.polimi.ingsw.messages.server;

import it.polimi.ingsw.messages.ServerMessage;
import it.polimi.ingsw.server.ClientDescriptor;
import it.polimi.ingsw.server.iServersideController;

public class PingMessage extends ServerMessage{

    @Override
    public void recieve(ClientDescriptor client, iServersideController controller) {
        controller.ping(client);
    }
    
}
