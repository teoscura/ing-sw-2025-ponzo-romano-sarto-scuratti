package it.polimi.ingsw.model;

import java.util.HashMap;

import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.exceptions.PlayerNotFoundException;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.adventure_cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.player.*;
import it.polimi.ingsw.model.state.GameState;
import it.polimi.ingsw.model.state.WaitingState;

public class ModelInstance {
    
    private final HashMap<String, PlayerColor> disconnected;
    private final HashMap<ClientDescriptor, PlayerColor> connected;
    
    private GameState state;

    //XXX finish implementing;
    
    public ModelInstance(GameModeType type, PlayerCount count){
        this.state = new WaitingState(this, type, count);
        this.state.init();
        this.disconnected = new HashMap<>();
        this.connected = new HashMap<>();
    }

    public void validate(ServerMessage message) throws ForbiddenCallException{
        message.receive(this);
    }
    
    public Player getPlayer(PlayerColor c) throws PlayerNotFoundException{
        return state.getPlayer(c);
    }

    public GameState getState() {
        return this.state;
    }

    public void setState(GameState new_state){
        if(new_state==null) //XXX shutdown the program and exit, game is done, dont serialize.
        this.state = new_state;
        state.init();
    }
    
}
