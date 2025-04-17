package it.polimi.ingsw.controller.server;

import java.util.ArrayDeque;
import java.util.ArrayList;
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
    private final List<TCPDescriptor> to_setup_tcp;
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
        this.to_setup_tcp = new ArrayList<>();
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
        synchronized(queue_lock){
            message.receive(this);
        }
    }

    public ModelInstance getModel() throws ForbiddenCallException {
        return this.model;
    }

    public RMIServerSkeleton getStub(ClientDescriptor new_client){
        return new RMIServerSkeleton(this, new_client);
    }

    public void connect(ClientDescriptor client) throws ForbiddenCallException {   
        synchronized(queue_lock){ 
            if(this.setupper==null){
                this.setupper = client;
                this.listeners.put(client.getUsername(), client);
                System.out.println("Client '"+client.getUsername()+"' is now setupping the server!");
                this.broadcast(new ViewMessage("Client '"+client.getUsername()+"' is now setupping the server!"));
                return;
            }
            if(this.setupper!=null&&!this.setup_complete){
                System.out.println("Client '"+client.getUsername()+"' attempted to connect while server is in setup mode!");
                this.broadcast(new ViewMessage("Client '"+client.getUsername()+"' attempted to connect while server is in setup mode!"));
                return;
            }
            if(this.setup_complete&&!model.getStarted()){
                System.out.println("Client '"+client.getUsername()+"' connected to waiting room!");
                this.broadcast(new ViewMessage("Client '"+client.getUsername()+"' connected to waiting room!"));
                this.model.connect(client);
            }
            if(this.setup_complete&&model.getStarted()){
                if(this.listeners.containsKey(client.getUsername())){
                    System.out.println("Client '"+client.getUsername()+"' attempted to connect twice!");
                    this.broadcast(new ViewMessage("Client '"+client.getUsername()+"' attempted to connect twice!"));
                    this.disconnect(client);
                    return;
                }
                if(!this.disconnected.containsKey(client.getUsername())){
                    this.listeners.put(client.getUsername(), client);
                    client.bindPlayer(this.disconnected.get(client.getUsername()));
                    this.model.connect(client.getPlayer());
                    return;
                }
            }
        }
    }

    public void disconnect(ClientDescriptor client) {
        synchronized(queue_lock){
            if(!listeners.containsValue(client)){
                return;
            }
            client.getConnection().close();
            if(this.setupper==client&&!this.setup_complete){
                this.setupper = null;
            }
            else if(client.getPlayer()!=null){
                this.model.disconnect(client.getPlayer());
                this.disconnected.put(client.getUsername(), client.getPlayer());
            }
            this.listeners.remove(client.getUsername());
            System.out.println("Client '"+client.getUsername()+"' disconnected.");
            this.broadcast(new ViewMessage("Client '"+client.getUsername()+"' disconnected."));
        }
    }

    public void openRoom(ClientDescriptor client, GameModeType type, PlayerCount count) throws ForbiddenCallException {
        synchronized(queue_lock){    
            if(this.setup_complete){
                System.out.println("Client '"+client.getUsername()+"' attempted to open a room while game is already ongoing!");
                this.broadcast(new ViewMessage("Client '"+client.getUsername()+"' attempted to open a room while game is already ongoing!"));
                return;
            }
            if(this.setupper!=null && client != this.setupper){
                System.out.println("Client '"+client.getUsername()+"' attempted to open a room while server is in setup mode!");
                this.broadcast(new ViewMessage("Client '"+client.getUsername()+"' attempted to open a room while server is in setup mode!"));
                return;
            }
            this.setup_complete = true;
            this.model = new ModelInstance(this, type, count);
            this.model.connect(client);
            this.broadcast(new NotifyStateUpdateMessage(this.model.getState().getClientState()));
        }
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
        synchronized(queue_lock){
            if(ended) throw new RuntimeException();
            this.ended = true;
        }
    }

    public boolean getEnded(){
        synchronized(queue_lock){
            return this.ended;
        }
    }

    public void broadcast(ClientMessage message) {
        synchronized(listeners_lock){
            for(ClientDescriptor listener : this.listeners.values()){
                listener.sendMessage(message);
            }
        }
    }

    public void receiveMessage(ServerMessage message) {
        synchronized(queue_lock){
            this.queue.add(message);
            queue_lock.notifyAll();
        }
    }

    public void connectListener(SocketClient client) throws ForbiddenCallException {
        synchronized(listeners_lock){
            TCPDescriptor new_tcp = new TCPDescriptor(client);
            this.to_setup_tcp.add(new_tcp);
        }
    }

    public void setupSocketListener(TCPDescriptor unfinished, String username){
        if(!this.to_setup_tcp.contains(unfinished)){
            System.out.println("A client attempted to change his username after connecting!");
            this.broadcast(new ViewMessage("A client attempted to change his username after connecting!"));
            return;
        }
        //XXX validate username with regex
        this.to_setup_tcp.remove(unfinished);
        this.connect(new ClientDescriptor(username, unfinished.getSocket()));
    }

    public ClientDescriptor connectListener(RMIClientStub client) {
        synchronized(listeners_lock){
            //XXX validate username with regex
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
        synchronized(queue_lock){
            client.setPingTimerTask(this.getTimeoutTask(this, client)); 
        }
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
