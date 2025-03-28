package it.polimi.ingsw.controller.messages.server;

import it.polimi.ingsw.controller.messages.ServerMessage;
import it.polimi.ingsw.server.ClientDescriptor;
import it.polimi.ingsw.server.iServerController;

public class PingMessage extends ServerMessage{

    @Override
    public void recieve(ClientDescriptor client, iServerController controller) {
        controller.ping(client);
    }

}
