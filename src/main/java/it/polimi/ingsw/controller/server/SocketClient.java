package it.polimi.ingsw.controller.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import it.polimi.ingsw.message.client.ClientMessage;
import it.polimi.ingsw.message.server.ServerMessage;

public class SocketClient implements Connection {

    private final Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean is_setup = false;

    public SocketClient(Socket socket) throws IOException{
        if(socket == null) throw new NullPointerException();
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
    }

    public Socket getSocket(){
        return this.socket;
    }

    @Override
    public void sendMessage(ClientMessage message) {
        try {
            this.out.reset();
            this.out.writeObject(message);
            this.out.flush();
        } catch (IOException e) {
            System.out.println("Failed to write object to: "+socket.getInetAddress());    
            e.printStackTrace();
        }
    }

    public void read(ServerController controller) {
        ServerMessage message = null;
        try {
            message = (ServerMessage) in.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){
            System.out.println("Failed to read object from: "+socket.getInetAddress());    
            e.printStackTrace();
        }
        controller.receiveMessage(message);
    }

    @Override
    public void close(){
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
