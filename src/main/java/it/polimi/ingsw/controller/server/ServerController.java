package it.polimi.ingsw.controller.server;

import java.util.List;
import java.util.concurrent.locks.Lock;

import it.polimi.ingsw.message.client.PingMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.adventure_cards.exceptions.ForbiddenCallException;
public class ServerController implements iServerController {
    
    private ModelInstance model = null;
    private Server server = Server.getInstance();
    private ClientDescriptor setupper;
    private Lock model_lock;
    
    public ServerController(){
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

    @Override
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

    @Override
    public void disconnect(ClientDescriptor client) {
        XXX
        if(this.model==null && client!=setupper){
            return;
        }
    }

    @Override
    public void openRoom(ClientDescriptor client, GameModeType type, PlayerCount count) {
        if(this.model!=null||client!=setupper){
            client.sendMessage(new ViewMessage("Server currently is in setup process!"));
            throw new ForbiddenCallException();
        }
        this.model = new ModelInstance(type, count);
        this.setupper = null;
        this.model.connect(client);
    }

    @Override
    public void getUnfinishedList(ClientDescriptor client) throws ForbiddenCallException {
        if(this.model!=null||client!=setupper) throw new ForbiddenCallException();
        //XXX Show list
    }

    @Override
    public void getMyUnfinishedList(ClientDescriptor client) throws ForbiddenCallException {
        if(this.model!=null||client!=setupper) throw new ForbiddenCallException();
        //XXX
    }

    // private List<JsonState> getUnfinishedGames(){
    //     return null;
    //     //XXX
    // }

    @Override
    public void openUnfinished(ClientDescriptor client, int id) {
        if(this.model!=null||client!=setupper){
            client.sendMessage(new ViewMessage("Server currently is in setup process!"));
            throw new ForbiddenCallException();
        }
        this.model = /*Load from json with id provided, error if not avail.*/
        this.model.connect(client);
    };

    @Override
    public void ping(ClientDescriptor client) {
        if(this.model!=null||client!=setupper) throw new ForbiddenCallException();
        client.resetTimer(); 
    }

}
