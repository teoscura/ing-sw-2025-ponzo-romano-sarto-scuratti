package it.polimi.ingsw.controller.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import it.polimi.ingsw.message.client.ClientMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.message.server.UsernameSetupMessage;

public class SocketClient implements Connection {

    private final Socket socket;
    private String username;
    private ObjectOutputStream out;
    private ObjectInputStream in;

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
    public void sendMessage(ClientMessage message) throws IOException {
        this.out.reset();
        this.out.writeObject(message);
        this.out.flush();
    }

    public void read(ServerController controller) {
        ServerMessage message = null;
        try {
            message = (ServerMessage) in.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){
            System.out.println("Failed to read object from: "+socket.getInetAddress()+", closing socket."); 
            this.close();
            e.printStackTrace();
        }
        if(message.getDescriptor()!=null){
            message.setDescriptor(null);
        }
        if(username==null){
            UsernameSetupMessage setup = null;
            try{
                setup = (UsernameSetupMessage) message;
            } catch (ClassCastException e) {
                System.out.println("Recieved non-setup message from tcp socket: "+socket.getInetAddress());    
                e.printStackTrace();
            }
            this.username = setup.getUsername();
            controller.setupSocketListener(this, this.username);
            return;
        }
        message.setDescriptor(controller.getDescriptor(this.username));
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
