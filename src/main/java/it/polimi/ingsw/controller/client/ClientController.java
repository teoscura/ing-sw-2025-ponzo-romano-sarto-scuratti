package it.polimi.ingsw.controller.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import it.polimi.ingsw.controller.server.rmi.RMISkeletonProvider;
import it.polimi.ingsw.message.client.ClientMessage;
import it.polimi.ingsw.message.server.PingMessage;
import it.polimi.ingsw.message.server.ServerDisconnectMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.view.ClientView;
import it.polimi.ingsw.view.ViewType;

public class ClientController {

    private final ClientView view;
    private final String username;
    private final Queue<ClientMessage> queue;
    private ServerConnection connection;

    private final Object queue_lock;

    public ClientController(ClientView view, ConnectionType ctype, String server_ip, int server_port, String username){
        if(view == null || username == null || server_ip == null) throw new NullPointerException();
        this.view = view;
        this.username = username;
        this.queue = new ArrayDeque<>();
        this.queue_lock = new Object();
        if(ctype==ConnectionType.SOCKET){
            SocketConnection tmp = null;
            try {
                tmp = new SocketConnection(this, server_ip, server_port);
                this.connection = tmp;
                tmp.start();
            } catch (IOException e) {
                System.out.println("Failed to connect to server, terminating.");
                e.printStackTrace();
                this.shutdown();
            }
        }
        else{
            try {
                this.connection = new RMIConnection(this, server_ip, username);
            } catch (RemoteException e) {
                System.out.println("Failed to connect to server, terminating.");
                e.printStackTrace();
                this.shutdown();
            } catch (NotBoundException e) {
                System.out.println("Selected server does not host game, terminating.");
                e.printStackTrace();
                this.shutdown();
            }
        }
        this.ping();
    }

    public void showTextMessage(String message) {
        this.view.showTextMessage(message);
    }

    public void sendMessage(ServerMessage message) {
        try {
            this.connection.sendMessage(message);
        } catch (IOException e) {
            System.out.println("Failed to send a message, terminating program!");
            this.connection.close();
            this.shutdown();
        }
    }

    public void ping(){
        this.sendMessage(new PingMessage());
        this.setPingTask();
    }

    private void setPingTask() {
        Timer t = new Timer(true);
        t.schedule(this.getPingTask(this), 100);
    }

    private TimerTask getPingTask(ClientController controller) {
        return new TimerTask(){
            public void run(){
                controller.sendMessage(new PingMessage());
            }
        };
    }

    public ClientView getView() {
        return this.view;
    }

    public void disconnect() {
        try {
            this.connection.sendMessage(new ServerDisconnectMessage());
        } catch (RemoteException e) {
            //Redundant, server is closing anyways
        } catch (IOException e) {
            //Redundant, server is closing anyways
        }
        this.connection.close();
        this.shutdown();
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

    public void shutdown(){
        this.connection.close();
        System.exit(-1);
    }

}
