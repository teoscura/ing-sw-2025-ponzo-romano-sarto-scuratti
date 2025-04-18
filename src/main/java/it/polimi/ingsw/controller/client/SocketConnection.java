package it.polimi.ingsw.controller.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import it.polimi.ingsw.controller.server.ServerController;
import it.polimi.ingsw.message.client.ClientMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.message.server.UsernameSetupMessage;

public class SocketConnection implements ServerConnection {

    private final ClientController controller;
    private final Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public SocketConnection(ClientController controller, Socket socket) throws IOException{
        if(controller == null || socket == null) throw new NullPointerException();
        this.controller = controller;
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
    }

    @Override
    public void connect(String server_ip) {
        // XXX
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
        ClientMessage message = null;
        try {
            message = (ClientMessage) in.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){
            System.out.println("Failed to read object from: "+socket.getInetAddress()+", closing socket."); 
            this.close();
            e.printStackTrace();
        }
        controller.receiveMessage(message);
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
