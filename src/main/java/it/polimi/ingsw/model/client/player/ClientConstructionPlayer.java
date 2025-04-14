package it.polimi.ingsw.model.client.player;

import java.util.List;

import it.polimi.ingsw.model.client.ClientSpaceShip;
import it.polimi.ingsw.model.player.PlayerColor;

public class ClientConstructionPlayer {
    
    private final String username;
    private final PlayerColor color;
    private final ClientSpaceShip ship;
    private final List<Integer> reserved_components;
    private final boolean finished;

    public ClientConstructionPlayer(String username, PlayerColor color, ClientSpaceShip ship, 
                                    List<Integer> reserved_components, boolean finished){
        if(username == null || ship==null || reserved_components==null || color == PlayerColor.NONE) throw new NullPointerException();
        if(reserved_components.size() > 3) throw new IllegalArgumentException();
        this.username = username;
        this.color = color;
        this.ship = ship;
        this.reserved_components = reserved_components;
        this.finished = finished;
    }

    public String getUsername(){
        return this.username;
    }

    public PlayerColor getColor(){
        return this.color;
    }

    public ClientSpaceShip getShip(){
        return this.ship;
    }

    public List<Integer> getReserved(){
        return this.reserved_components;
    }

    public boolean isFinished(){
        return this.finished;
    }

}
