package it.polimi.ingsw.messages.server;

import it.polimi.ingsw.messages.ServerMessage;
import it.polimi.ingsw.server.ClientDescriptor;
import it.polimi.ingsw.server.iServerController;

public class PingMessage extends ServerMessage{

    @Override
    public void recieve(ClientDescriptor client, iServerController controller) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'recieve'");
    }
    
}
