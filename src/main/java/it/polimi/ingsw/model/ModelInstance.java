package it.polimi.ingsw.model;

import java.util.HashMap;
import java.util.List;

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

    private GameState state;
    
    public ModelInstance(ServerController server, GameModeType type, PlayerCount count){
        this.controller = server;
        this.state = new WaitingState(this, type, count);
        this.state.init();
    }

    public synchronized void validate(ServerMessage message){
        try {
            message.receive(this);
        } catch (ForbiddenCallException e) {
            System.out.println("Player " + message.getDescriptor().getUsername() + " attempted a forbidden command in the current state of the game!");
        }
    }

    public synchronized void startGame(List<Player> players){
        this.controller.startGame(players);
    }
    
    // private synchronized Player getPlayer(PlayerColor c){
    //     Player p = null;
    //     try {
    //         p = this.state.getPlayer(c);
    //     } catch (PlayerNotFoundException e) {
    //         System.out.println("Player with the color "+ c.toString() + " is not playing!");
    //     }
    //     return p;
    // }

    public synchronized GameState getState() {
        return this.state;
    }

    public synchronized void setState(GameState new_state){
        xxx    
    }

    public synchronized void connect(ClientDescriptor client){
        xxx
    }

    public synchronized void disconnect(ClientDescriptor client){
        xxx
    }

    public synchronized void kick(ClientDescriptor client){
        try {
            this.state.disconnect(client);
        } catch (ForbiddenCallException e) {
            System.out.println("Player " + client.getUsername() + " is not connected, cannot kick!");
        }
        this.controller.kick(client);
    }
    
}
