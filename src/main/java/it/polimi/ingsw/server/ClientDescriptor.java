package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.exceptions.UserAlreadyInGameException;
import it.polimi.ingsw.controller.exceptions.UserNotInGameException;
import it.polimi.ingsw.controller.match.MatchController;
import it.polimi.ingsw.net.ConnectionAdapter;

public class ClientDescriptor {
    private String username = null;
    private boolean in_game = false;
    private MatchController current_match;
    private ConnectionAdapter connection;

    public ClientDescriptor(String username, ConnectionAdapter connection){
        if(username==null || connection==null) throw new NullPointerException();
        this.username = username;
        this.connection = connection;
    }

    public String getUsername(){
        return this.username;
    }

    public boolean getInGame(){
        return this.in_game;
    }

    public long getMatchId(){
        if(this.current_match==null) return -1;
        return this.current_match.getMatchId();
    }
    
    public ConnectionAdapter getConnectionAdapter(){
        return this.connection;
    }

    public void setMatch(MatchController controller) throws UserAlreadyInGameException{
        if(this.in_game) throw new UserAlreadyInGameException("Selected user is already in game!");
        this.in_game = true;
        this.current_match = controller;
    }

    public void removeFromGame(String message) throws UserNotInGameException{
        //TODO: send client message and state change event;
        if(!this.in_game) throw new UserNotInGameException("The user: \""+this.username+"\" is not in a ongoing game or waiting room!");
        this.in_game = false;
        this.current_match = null;
    }
    
}
