package it.polimi.ingsw.controller.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import it.polimi.ingsw.controller.server.ServerController;
import it.polimi.ingsw.message.server.ServerMessage;

public class SocketConnection implements ServerConnection {

    private final ServerController controller;
    private final Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public SocketConnection(ServerController controller, Socket socket) throws IOException{
        if(controller == null || socket == null) throw new NullPointerException();
        this.controller = controller;
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
    }

    @Override
    public void sendMessage(ServerMessage message) {
        try {
            this.out.reset();
            this.out.writeObject(message);
            this.out.flush();
        } catch (IOException e) {
            System.out.println("Failed to write object to: "+socket.getInetAddress());  
            e.printStackTrace();
        }
    }

    @Override
    public void read(){
        
    }

    @Override
    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
