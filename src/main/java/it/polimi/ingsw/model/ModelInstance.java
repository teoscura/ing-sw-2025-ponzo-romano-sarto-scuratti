package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.PlayerNotFoundException;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.adventure_cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.player.*;
import it.polimi.ingsw.model.state.GameState;
import it.polimi.ingsw.model.state.WaitingState;

public class ModelInstance {
    
    private final Player[] players;
    
    private GameState state;

    //XXX finish implementing;
    
    public ModelInstance(GameModeType type, PlayerCount count){
        this.players = new Player[count.getNumber()];
        for(PlayerColor c : PlayerColor.values()){
            if(c.getOrder()>=players.length) break;
            this.players[c.getOrder()] = new Player(type, c);
        }
        this.state = new WaitingState(this, type, count);
        this.state.init();
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
        this.state = new_state;
        state.init();
    }
    
}
