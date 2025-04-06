package it.polimi.ingsw.model;

import java.util.HashMap;

import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.controller.server.Server;
import it.polimi.ingsw.controller.server.ServerController;
import it.polimi.ingsw.exceptions.PlayerNotFoundException;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.adventure_cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.player.*;
import it.polimi.ingsw.model.state.GameState;
import it.polimi.ingsw.model.state.WaitingState;

public class ModelInstance {
    
    private final ServerController controller;

    private final HashMap<ClientDescriptor, PlayerColor> connected;
    private final HashMap<String, PlayerColor> disconnected;

    private GameState state;
    private Object state_lock;
    
    public ModelInstance(ServerController server, GameModeType type, PlayerCount count){
        this.controller = server;
        this.disconnected = new HashMap<>();
        this.connected = new HashMap<>();
        this.state = new WaitingState(this, type, count);
        this.state.init();
        
    }

    public void validate(ServerMessage message) throws ForbiddenCallException{
        message.receive(this);
    }
    
    public Player getPlayer(PlayerColor c) throws PlayerNotFoundException{
        synchronized(state_lock){
            return state.getPlayer(c);
        }
    }

    public GameState getState() {
        synchronized(state_lock){
            return this.state;
        }
    }

    public void setState(GameState new_state){
        synchronized(state_lock){
            if(new_state==null){
                
            }
            this.state = new_state;
            state.init();
        }
    }

    public void connect(ClientDescriptor client){
        if(this.connected.containsKey(client)){
            client.sendMessage(new ViewMessage("You are already connected!"));
            return;
        }
        //aa
    }

    public void disconnect(ClientDescriptor client){
        if(!this.connected.containsKey(client)){
            client.sendMessage(new ViewMessage("You aren't connected!"));
            return;
        }
        this.connected.remove(client);
        synchronized(this.state_lock){
            try {
                this.state.disconnect(client);
                this.disconnected.put(client.getUsername(), client.getPlayer().getColor());
            } catch (ForbiddenCallException e) {
                //No concrete state has this, no action needed;
            }
        }
    }

    public void kick(ClientDescriptor client){
        this.connected.remove(client);
        synchronized(this.state_lock){
            this.state.disconnect(client);
        }
        this.controller.kick(client);
    }
    
}
