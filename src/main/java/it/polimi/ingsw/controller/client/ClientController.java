package it.polimi.ingsw.controller.client;

import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayDeque;
import java.util.Queue;

import it.polimi.ingsw.controller.server.rmi.RMISkeletonProvider;
import it.polimi.ingsw.message.client.ClientMessage;
import it.polimi.ingsw.message.server.PingMessage;
import it.polimi.ingsw.message.server.ServerDisconnectMessage;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.view.ClientView;
import it.polimi.ingsw.view.ViewType;

public class ClientController {

    private final ClientView view;
    private final String username;
    private final Queue<ClientMessage> queue;
    private ServerConnection connection;

    private final Object queue_lock;

    public ClientController(ClientView view, ConnectionType ctype, String local_ip, String server_ip, String username){
        if(view == null || username == null || local_ip == null || server_ip == null) throw new NullPointerException();
        this.view = view;
        this.username = username;
        this.queue = new ArrayDeque<>();
        this.queue_lock = new Object();
        if(ctype==ConnectionType.SOCKET){
            this.connection = new SocketConnection(this, new Socket(local_ip, 10000));
            Socket socket = new Socket();
        }
        else{
            this.connection = new RMIConnection(this, server_ip, username);
        }

    }

    public void showTextMessage(String message) {
        this.view.showTextMessage(message);
    }

    public void ping() {
        this.connection.sendMessage(new PingMessage());
    }

    public ClientView getView() {
        return this.view;
    }

    public void disconnect() {
        this.connection.sendMessage(new ServerDisconnectMessage());
        this.connection.close();
    }

    public void kick(){
        this.connection.close();
        System.exit(2);
    }

    public void receiveMessage(ClientMessage message) {
        synchronized(queue_lock){
            this.queue.add(message);
            queue_lock.notifyAll();
        }
    }

}
