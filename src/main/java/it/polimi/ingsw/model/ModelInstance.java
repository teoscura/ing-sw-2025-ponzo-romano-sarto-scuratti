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
    private boolean started;
    private GameState state;
    
    public ModelInstance(ServerController server, GameModeType type, PlayerCount count){
        this.controller = server;
        this.state = new WaitingState(this, type, count);
        this.state.init();
    }

    public void validate(ServerMessage message) throws ForbiddenCallException{
        message.receive(this);
    }

    public void serialize(){
        //XXX figure out how to give a pathname to it, write to that file using json.
    }

    public void startGame(List<Player> players) throws ForbiddenCallException{
        if(this.started) throw new ForbiddenCallException();
        this.started = true;
    }

    public boolean getStarted(){
        return this.started;
    }

    public void endGame() {
        if(!this.started) throw new RuntimeException();
        this.controller.endGame();
    }

    public GameState getState() {
        return this.state;
    }

    public void setState(GameState next){
        if(next==null){
            this.endGame();
        }
        this.state = next;
        this.serialize();
        next.init();
    }

    public void connect(ClientDescriptor client){
        try{
            this.state.connect(client);
        }
        catch (ForbiddenCallException e) { 
            System.out.println("Client: '" + client.getUsername() + "' tried connecting when the current state doesn't support it anymore!");
        }
    }

    public void disconnect(ClientDescriptor client){
        try{
            this.state.disconnect(client);
        }
        catch (ForbiddenCallException e) { 
            System.out.println("Client: '" + client.getUsername() + "' tried disconnecting when the current state doesn't support it anymore!");
        }
    }

    public void connect(Player p){
        try{
            this.state.disconnect(p);
            System.out.println("Client: '" + p.getUsername() + "' reconnected to the game!");
        }
        catch (ForbiddenCallException e) { 
            System.out.println("Client: '" + p.getUsername() + "' tried reconnecting when the current state doesn't support it anymore!");
        }
    }

    public void disconnect(Player p){
        try{
            this.state.disconnect(p);
            System.out.println("Client: '" + p.getUsername() + "' disconnected from the game!");
        }
        catch (ForbiddenCallException e) { 
            System.out.println("Client: '" + p.getUsername() + "' tried disconnecting when the current state doesn't support it anymore!");
        }
    }

    public void kick(ClientDescriptor client){
        try {
            this.state.disconnect(client);
        } catch (ForbiddenCallException e) {
            System.out.println("Player " + client.getUsername() + " is not connected, cannot kick!");
        }
        this.controller.kick(client);
    }

    public ServerController getController() {
        return this.controller;
    }
    
}
