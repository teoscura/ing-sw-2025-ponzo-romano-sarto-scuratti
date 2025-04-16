package it.polimi.ingsw.controller.server;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;

import it.polimi.ingsw.controller.client.RMIServerSkeleton;
import it.polimi.ingsw.controller.client.RemoteServer;
import it.polimi.ingsw.message.client.ClientMessage;
import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.client.ClientDisconnectMessage;
import it.polimi.ingsw.message.server.ServerConnectMessage;
import it.polimi.ingsw.message.server.ServerDisconnectMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.adventure_cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;

public class ServerController extends Thread implements RemoteServer {

    private ModelInstance model = null;
    private final Server server;
    private final HashMap<String, ClientDescriptor> listeners;
    private final HashMap<String, Player> disconnected;
    private final Queue<ServerMessage> queue;

    private ClientDescriptor setupper;
    private boolean setup_complete = false;
    private boolean ended = false;

    private Object listeners_lock;
    private Object queue_lock;

    public ServerController(){
        this.server = Server.getInstance();
        this.server.setController(this);
        this.listeners = new HashMap<>();
        this.disconnected = new HashMap<>();
        this.queue = new ArrayDeque<>();
        this.server.setDaemon(true);
        this.server.start();
        /*Load all jsons in list that are valid.*/;
    }

    @Override
    public void run(){
        while(true){
            synchronized(queue_lock){
                if(this.queue.isEmpty())
                    try {
                        queue_lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                while(!queue.isEmpty()){
                    ServerMessage message = this.queue.poll();
                    try {
                        this.validate(message);
                    } catch (ForbiddenCallException e) {
                        System.out.println("Client: '"+message.getDescriptor().getUsername()+"' attempted a forbidden command!");
                        e.printStackTrace();
                    }
                }
                queue_lock.notifyAll();
            }
        }
    }

    public void validate(ServerMessage message) throws ForbiddenCallException{
        message.receive(this);
    }

    public ModelInstance getModel() throws ForbiddenCallException {
        return this.model;
    }

    public RMIServerSkeleton getStub(ClientDescriptor new_client){
        return new RMIServerSkeleton(this, new_client);
    }

    public void connect(ClientDescriptor client) throws ForbiddenCallException {   
        if(this.setupper!=null&&!this.setup_complete){
            System.out.println("Client '"+client.getUsername()+"' attempted to connect while server is in setup mode!");
            this.broadcast(new ViewMessage("Client '"+client.getUsername()+"' attempted to connect while server is in setup mode!"));
            return;
        }
        x;
    }

    public void disconnect(ClientDescriptor client) {
        synchronized(queue_lock){
            if(!listeners.containsValue(client)){
                return;
            }
            this.listeners.remove(client.getUsername());
            if(client.getPlayer()!=null){
                this.model.disconnect(client.getPlayer());
                this.disconnected.put(client.getUsername(), client.getPlayer());
            }
            client.getConnection().close();
        }
    }

    public void openRoom(ClientDescriptor client, GameModeType type, PlayerCount count) throws ForbiddenCallException {
        if(this.setup_complete){
            System.out.println("Client '"+client.getUsername()+"' attempted to open a room while game is already ongoing!");
            this.broadcast(new ViewMessage("Client '"+client.getUsername()+"' attempted to open a room while game is already ongoing!"));
            return;
        }
        if(this.setupper!=null){
            System.out.println("Client '"+client.getUsername()+"' attempted to open a room while server is in setup mode!");
            this.broadcast(new ViewMessage("Client '"+client.getUsername()+"' attempted to open a room while server is in setup mode!"));
            return;
        }
        this.setup_complete = true;
        this.setupper = client;
        this.model = new ModelInstance(this, type, count);
        this.model.validate(new ServerConnectMessage(client));
        this.broadcast(new NotifyStateUpdateMessage(this.model.getState().getClientState()));
    }

    // public void getUnfinishedList(ClientDescriptor client) throws ForbiddenCallException {
    //     XXX;
    // }

    // public void openUnfinished(ClientDescriptor client, int id) {
    //     XXX;
    // };

    public void kick(ClientDescriptor client) {
        client.sendMessage(new ClientDisconnectMessage());
        this.listeners.remove(client.getUsername());
        client.getConnection().close();
    }

    public void endGame(){
        if(ended) throw new RuntimeException();
        this.ended = true;
    }

    public boolean getEnded(){
        return this.ended;
    }

    public void broadcast(ClientMessage message) {
        for(ClientDescriptor listener : this.listeners.values()){
            listener.sendMessage(message);
        }
    }

    public void receiveMessage(ServerMessage message) {
        synchronized(queue_lock){
            this.queue.add(message);
            queue_lock.notifyAll();
        }
    }

    public void connectListener(SocketClient client) throws ForbiddenCallException {
        if(client.getUsername()==null) throw new ForbiddenCallException("Attempted to connect from address '"+client.getSocket().getInetAddress()+"' while not having finished correct TCP client setup!");
        ClientDescriptor new_listener = new ClientDescriptor(client.getUsername(), client);
        try {
            this.connect(new_listener);
        } catch (ForbiddenCallException e) {
            System.out.println("Client '"+client.getUsername()+"' failed to connect properly!");
            return;
        }
    }

    public ClientDescriptor connectListener(RMIClientStub client) {
        synchronized(listeners_lock){
            ClientDescriptor new_listener = new ClientDescriptor(client.getUsername(), client);
            if(this.listeners.containsKey(client.getUsername())) return null;
            try {
                this.connect(new_listener);
            } catch (ForbiddenCallException e) {
                System.out.println("Client '"+client.getUsername()+"' failed to connect properly!");
                return null;
            }
            return new_listener;
        }
    }

    public void ping(ClientDescriptor client) {
        client.setPingTimerTask(this.getTimeoutTask(this, client)); 
    }

    protected TimerTask getTimeoutTask(ServerController controller, ClientDescriptor client){
        return new TimerTask(){
            public void run(){
                System.out.println("Client '"+client.getUsername()+"' failed to ping in between timeout!");
                controller.disconnect(client);
            }
        };
    }

}
