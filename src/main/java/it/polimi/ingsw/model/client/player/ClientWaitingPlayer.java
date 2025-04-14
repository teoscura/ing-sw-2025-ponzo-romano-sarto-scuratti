package it.polimi.ingsw.model.client.player;

import it.polimi.ingsw.model.player.PlayerColor;

public class ClientWaitingPlayer {
    
    private final String username;
    private final PlayerColor color;

    public ClientWaitingPlayer(String username, PlayerColor color){
        if(username == null || color == PlayerColor.NONE) throw new NullPointerException();
        this.username = username;
        this.color = color;
    }

    public String getUsername(){
        return this.username;
    }

    public PlayerColor getColor(){
        return this.color;
    }
}
