package it.polimi.ingsw.controller.server;

public class TCPDescriptor {
    
    private final SocketClient socket;

    public TCPDescriptor(SocketClient socket){
        if(socket==null) throw new NullPointerException();
        this.socket = socket;
    }

    public SocketClient getSocket(){
        return this.socket;
    }
}
