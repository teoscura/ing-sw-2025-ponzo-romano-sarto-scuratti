package it.polimi.ingsw.controller.client;

import it.polimi.ingsw.message.server.ServerMessage;

public class SocketConnection implements ServerConnection {

    @Override
    public void sendMessage(ServerMessage message) {
        //XXX serialize message, send to socket.
    }

    @Override
    public void close() {
        //XXX
    }
    
}
