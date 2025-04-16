package it.polimi.ingsw.controller.server;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.Lock;

import it.polimi.ingsw.controller.client.RMIServerStub;
import it.polimi.ingsw.message.client.ClientMessage;
import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.PingMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.adventure_cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;

public class ServerController implements RMIServerStub {
    
    private ModelInstance model = null;
    private final Server server;

    private final HashMap<ClientDescriptor, PlayerColor> connected;
    private final HashMap<String, PlayerColor> disconnected;
    private boolean started = false;
    
    private ClientDescriptor setupper;
    private Lock model_lock;
    
    public ServerController(){
        this.server = Server.getInstance();
        this.server.setController(this);
        /*Load all jsons in list that are valid.*/;
        //run server, set as daemon;
        //Do everything.
    }

    public ModelInstance getModel() throws ForbiddenCallException {
        synchronized(this.model_lock){
            if(this.model==null) throw new ForbiddenCallException();
            return this.model;
        }
    }

    public RMIServerStub getStub(RMIClientStub client){
        //XXX aggiungi a utenti connessi, se qualcuno connesso e setup non finito butta via.
        //se Username duplicato rifiuta.
    }

    public void connect(ClientDescriptor client) throws ForbiddenCallException {   
        if(this.model==null && setupper!=null ){
            client.sendMessage(new ViewMessage("Server currently is in setup process!"));
            throw new ForbiddenCallException();
        }
        if(this.model==null){
            //Enter setup
        }
        this.model.connect(client);
    }

    public void disconnect(ClientDescriptor client) {
        XXX
    }

    public void openRoom(ClientDescriptor client, GameModeType type, PlayerCount count) {
        if(this.model!=null||client!=setupper){
            client.sendMessage(new ViewMessage("Server currently is in setup process!"));
            throw new ForbiddenCallException();
        }
        this.model = new ModelInstance(this, type, count);
        this.setupper = null;
        this.model.connect(client);
    }

    public void getUnfinishedList(ClientDescriptor client) throws ForbiddenCallException {
        if(this.model!=null||client!=setupper) throw new ForbiddenCallException();
        //XXX Show list
    }

    public void getMyUnfinishedList(ClientDescriptor client) throws ForbiddenCallException {
        if(this.model!=null||client!=setupper) throw new ForbiddenCallException();
        //XXX
    }

    // private List<JsonState> getUnfinishedGames(){
    //     return null;
    //     //XXX
    // }

    public void openUnfinished(ClientDescriptor client, int id) {
        if(this.model!=null||client!=setupper){
            client.sendMessage(new ViewMessage("Server currently is in setup process!"));
            throw new ForbiddenCallException();
        }
        if(this.connected.containsKey(client)){
            client.sendMessage(new ViewMessage("Cannot connect twice from the same connection!"));
            throw new ForbiddenCallException();
        }
        this.model = /*Load from json with id provided, error if not avail.*/
        this.model.connect(client);
    };

    public void ping(ClientDescriptor client) {
        if(this.model!=null||client!=setupper) throw new ForbiddenCallException();
        client.ping(); 
    }

    public void startGame(List<Player> players) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'startGame'");
    }

    public void kick(ClientDescriptor client) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'kick'");
    }

    public void endGame() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'endGame'");
    }

    public void broadcast(ClientMessage message) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'broadcast'");
    }

}
