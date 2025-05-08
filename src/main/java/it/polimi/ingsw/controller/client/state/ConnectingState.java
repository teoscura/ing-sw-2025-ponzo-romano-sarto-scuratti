package it.polimi.ingsw.controller.client.state;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import it.polimi.ingsw.controller.client.ClientController;
import it.polimi.ingsw.controller.client.ThreadSafeMessageQueue;
import it.polimi.ingsw.controller.client.connections.ConnectionType;
import it.polimi.ingsw.controller.client.connections.RMIConnection;
import it.polimi.ingsw.controller.client.connections.ServerConnection;
import it.polimi.ingsw.controller.client.connections.SocketConnection;
import it.polimi.ingsw.message.server.UsernameSetupMessage;
import it.polimi.ingsw.view.ClientView;

public class ConnectingState extends ClientControllerState {

    private final String username;
    private ServerConnection connection = null;
    private ThreadSafeMessageQueue inqueue;

    public ConnectingState(ClientController controller, ClientView view, String username){
        super(controller, view);
        if(username == null) throw new NullPointerException();
        this.username = username;
    }

    @Override
    public void init() {
        view.showConnectionScreen(this); 
    }

    public ClientController getController(){
        return this.controller;
    }

    @Override
    public ClientControllerState getNext() {
        if (this.connection == null) return new TitleScreenState(controller, view);
        else return new ConnectedState(controller, view, username, connection, this.inqueue);
    }

    public void connect(String address, int port, ConnectionType type){
        this.inqueue = new ThreadSafeMessageQueue();
        switch(type){
            case ConnectionType.RMI: {
                try {
                    this.connection = new RMIConnection(inqueue, address, username, port);
                } catch (RemoteException e) {
                    System.out.println("Failed to connect to server, terminating.");
                    e.printStackTrace();
                } catch (NotBoundException e) {
                    System.out.println("Selected server does not host game, terminating.");
                    e.printStackTrace();
			    }
            }; break;
            case ConnectionType.SOCKET: {
                SocketConnection tmp = null;
                try {
                    tmp = new SocketConnection(inqueue, address, port);
                    tmp.start();
                    tmp.sendMessage(new UsernameSetupMessage(this.username));
                    connection = tmp;
                } catch (IOException e) {
                    System.out.println("Failed to connect to server, going back to main screen.");
                    e.printStackTrace();
                }
            }; break;
            default: throw new UnsupportedOperationException();
        }
        this.transition();
    }
    
}
