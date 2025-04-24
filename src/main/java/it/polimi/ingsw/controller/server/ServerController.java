package it.polimi.ingsw.controller.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.polimi.ingsw.controller.client.RMIClientStub;
import it.polimi.ingsw.controller.server.rmi.RMIServerStubImpl;
import it.polimi.ingsw.controller.server.rmi.RemoteServer;
import it.polimi.ingsw.message.client.*;
import it.polimi.ingsw.message.server.*;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.adventure_cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;

public class ServerController extends Thread implements RemoteServer {

    private final Server server;
    private final HashMap<String, ClientDescriptor> listeners;
    private final HashMap<String, Player> disconnected;
    private final List<SocketClient> to_setup_tcp;
    private final Queue<ServerMessage> queue;
    private final int id;
    private final ArrayList<ModelInstance> unfinished;
    private ModelInstance model = null;
    private String serializer_path;
    private ClientDescriptor setupper;
    private boolean setup_complete = false;
    private boolean ended = false;

    private Object listeners_lock;
    private Object queue_lock;

    private Timer dsctimer = null;

    public ServerController(){
        this.server = new Server(this);
        this.listeners = new HashMap<>();
        this.disconnected = new HashMap<>();
        this.queue = new ArrayDeque<>();
        this.server.setDaemon(true);
        this.to_setup_tcp = new ArrayList<>();
        this.dsctimer = new Timer(true);
        this.unfinished = new ArrayList<>();
        Pattern saved_game_pattern = Pattern.compile("^gtunfinished-[0-9]+\\.gtuf$");
        String current_filepath = Paths.get("").toAbsolutePath().toString();
        File current_directory = new File(current_filepath);
        File[] files = current_directory.listFiles();
        for(File f : files){
            Matcher matcher = saved_game_pattern.matcher(f.getName());
            if(matcher.matches()){
                try(FileInputStream fis = new FileInputStream(f.getAbsolutePath());
                    ObjectInputStream ois = new ObjectInputStream(fis)){
                    ModelInstance loaded = (ModelInstance) ois.readObject();
                    unfinished.add(loaded);
                } catch (IOException e) {
                    System.out.println("Read error during loading of File: '"+f.getName());
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    System.out.println("File: '"+f.getName()+"' is not a valid savefile!");
                    e.printStackTrace();
                }
            }
        }
        if(unfinished.isEmpty()){
            this.id = 1;
        } else {
            this.id = this.unfinished.stream().mapToInt(i -> i.getID()).max().getAsInt();
        }
        this.server.start();
    }

    @Override
    public void run(){
        while(!this.ended){
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

    public RemoteServer getStub(ClientDescriptor new_client) throws RemoteException{
        return (RemoteServer) new RMIServerStubImpl(this, new_client);

    }

    public ClientDescriptor getDescriptor(String username) {
        return this.listeners.get(username);
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
            else if(this.setupper!=null&&!this.setup_complete){
                System.out.println("Client '"+client.getUsername()+"' attempted to connect while server is in setup mode!");
                this.broadcast(new ViewMessage("Client '"+client.getUsername()+"' attempted to connect while server is in setup mode!"));
                return;
            }
            if(this.setup_complete&&!model.getStarted()){
                System.out.println("Client '"+client.getUsername()+"' connected to waiting room!");
                this.broadcast(new ViewMessage("Client '"+client.getUsername()+"' connected to waiting room!"));
                this.model.connect(client);
            }
            else if(this.setup_complete&&model.getStarted()){
                if(this.listeners.containsKey(client.getUsername())){
                    System.out.println("Client '"+client.getUsername()+"' attempted to connect twice!");
                    this.broadcast(new ViewMessage("Client '"+client.getUsername()+"' attempted to connect twice!"));
                    this.disconnect(client);
                }
                else if(!this.disconnected.containsKey(client.getUsername())){
                    this.listeners.put(client.getUsername(), client);
                    this.disconnected.remove(client.getUsername());
                    client.bindPlayer(this.disconnected.get(client.getUsername()));
                    this.model.connect(client.getPlayer());
                    this.dsctimer.cancel(); 
                    this.dsctimer = null;
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
            if(this.disconnected.size()>=this.model.getState().getCount().getNumber()-1){
                this.dsctimer = new Timer(true);
                this.dsctimer.schedule(this.getEndMatchTask(this), 60000L);
            }
            else if(this.disconnected.size()==this.model.getState().getCount().getNumber()){
                this.dsctimer.cancel();
                this.endGame();
            }
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
            this.model = new ModelInstance(this.id, this, type, count);
            this.serializer_path = "gtunfinished-"+this.id+".gtuf";
            this.model.connect(client);
            this.broadcast(new NotifyStateUpdateMessage(this.model.getState().getClientState()));
        }
    }

    public void getUnfinishedList(ClientDescriptor client) throws ForbiddenCallException {
        synchronized(queue_lock){ 
            if(this.setup_complete){
                System.out.println("Client '"+client.getUsername()+"' asked for the list of unfinished games while a game is already ongoing!");
                this.broadcast(new ViewMessage("Client '"+client.getUsername()+"' asked for the list of unfinished games while a game is already ongoing!"));
                return;
            }
            if(this.setupper!=null && client != this.setupper){
                System.out.println("Client '"+client.getUsername()+"' asked for the list of unfinished games while the server is in setup mode!");
                this.broadcast(new ViewMessage("Client '"+client.getUsername()+"' asked for the list of unfinished games while the server is in setup mode!"));
                return;
            }
            String list = new String();
            for(ModelInstance m : this.unfinished){
                list.concat(m.toString() + "\n");
            }
            try {
                client.sendMessage(new ViewMessage(list));
            }  catch (IOException e){
                client.getConnection().close();
                this.disconnect(client);
            }
        }
    }

    public void openUnfinished(ClientDescriptor client, int id) {
        synchronized(queue_lock){    
            if(this.setup_complete){
                System.out.println("Client '"+client.getUsername()+"' attempted to open a unfinished game while a game is already ongoing!");
                this.broadcast(new ViewMessage("Client '"+client.getUsername()+"' attempted to open a unfinished game while a game is already ongoing!"));
                return;
            }
            if(this.setupper!=null && client != this.setupper){
                System.out.println("Client '"+client.getUsername()+"' attempted to open a unfinished game while server is in setup mode!");
                this.broadcast(new ViewMessage("Client '"+client.getUsername()+"' attempted to open a unfinished game while server is in setup mode!"));
                return;
            }
            this.setup_complete = true;
            this.model.afterSerialRestart();
            this.model = this.unfinished.get(id);
            this.serializer_path = "gtunfinished-"+id+".gtuf";
            this.model.connect(client);
            this.broadcast(new NotifyStateUpdateMessage(this.model.getState().getClientState()));
        }
    };

    public void kick(ClientDescriptor client) {
        try {
            client.sendMessage(new ClientDisconnectMessage());
        } catch (IOException e){
            //None
        } finally{
            this.disconnect(client);
        }
    }

    public void serializeCurrentGame(){
        synchronized(queue_lock){;
            try (FileOutputStream file = new FileOutputStream(this.serializer_path); 
                ObjectOutputStream oos = new ObjectOutputStream(file)) {
                oos.reset();
                oos.writeObject(this.model);
                oos.reset();
            } catch (IOException e) {
                System.out.println("Failed to serialize the current modelinstance, closing server!");
                this.endGame();
            }
        }
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
                try {
                    listener.sendMessage(message);
                }  catch (IOException e){
                    listener.getConnection().close();
                    this.disconnect(listener);
                }
            }
        }
    }

    public void receiveMessage(ServerMessage message) {
        if(message.getDescriptor()==null||this.listeners.containsKey(message.getDescriptor().getUsername())){
            System.out.println("Recieved a message from a client not properly connected!");
            this.broadcast(new ViewMessage("Recieved a message from a client not properly connected!"));
            return;
        }
        synchronized(queue_lock){
            this.queue.add(message);
            queue_lock.notifyAll();
        }
    }

    public void setupSocketListener(SocketClient client, String username){
        if(!this.to_setup_tcp.contains(client)){
            System.out.println("A client attempted to change his username after connecting!");
            this.broadcast(new ViewMessage("A client attempted to change his username after connecting!"));
            return;
        }
        this.to_setup_tcp.remove(client);
        if(!this.validateNewUsername(username)){
            System.out.println("A client attempted to connect with an invalid name!");
            return;
        }
        ClientDescriptor new_listener = new ClientDescriptor(username, client);
        try {
            this.connect(new_listener);
        } catch (ForbiddenCallException e) {
            System.out.println("Client: '"+username+"' failed to connect!");
            this.broadcast(new ViewMessage("Client: '"+username+"' failed to connect!"));
            return;
        }
        new_listener.setPingTimerTask(this.getTimeoutTask(this, new_listener));
    }

    public void connectListener(SocketClient client){
        synchronized(listeners_lock){
            if(this.to_setup_tcp.contains(client)){
                System.out.println("A client attempted to connect while already connecting!");
                this.broadcast(new ViewMessage("A client attempted to connect while already connecting!"));
                return;
            }
            this.to_setup_tcp.add(client);
        }
    }

    public ClientDescriptor connectListener(RMIClientStub client) {
        synchronized(listeners_lock){
            ClientDescriptor new_listener = new ClientDescriptor(client.getUsername(), client);
            if(this.listeners.containsKey(client.getUsername()) || !validateNewUsername(client.getUsername())) return null;
            try {
                this.connect(new_listener);
            } catch (ForbiddenCallException e) {
                System.out.println("Client '"+client.getUsername()+"' failed to connect properly!");
                return null;
            }
            new_listener.setPingTimerTask(this.getTimeoutTask(this, new_listener));
            return new_listener;
        }
    }

    private boolean validateNewUsername(String username){
        Pattern allowed = Pattern.compile("^[a-zA-Z0-9_.-]*$");
        Matcher matcher = allowed.matcher(username);
        return matcher.matches();
    }

    public void ping(ClientDescriptor client) {
        synchronized(queue_lock){
            client.setPingTimerTask(this.getTimeoutTask(this, client)); 
        }
    }

    private TimerTask getEndMatchTask(ServerController controller){
        return new TimerTask(){
            public void run(){
                System.out.println("Only one player was left for too long, closing the match!");
                controller.endGame();
            }
        };
    }

    private TimerTask getTimeoutTask(ServerController controller, ClientDescriptor client){
        return new TimerTask(){
            public void run(){
                synchronized(queue_lock){
                    System.out.println("Client '"+client.getUsername()+"' failed to ping in between timeout!");
                    controller.disconnect(client);
                }
            }
        };
    }

}
