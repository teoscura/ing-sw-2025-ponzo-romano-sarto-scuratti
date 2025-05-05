package it.polimi.ingsw.model.client;

import java.io.Serializable;
import java.util.HashMap;

import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.player.PlayerColor;

public class ClientGameListEntry implements Serializable {
    
    private final HashMap<PlayerColor, String> players;
    private final int model_id;
    private final GameModeType type;
    private final String state;

    public ClientGameListEntry(GameModeType type, String state, HashMap<PlayerColor, String> players, int model_id){
        if(players == null || type == null || state == null) throw new NullPointerException();
        if(players.size()<2 || players.size()>4 || model_id < 0) throw new IllegalArgumentException();
        this.players = players;
        this.model_id = model_id;
        this.type = type;
        this.state = state;
    }

    public GameModeType getType(){
        return this.type;
    }

    public HashMap<PlayerColor, String> getPlayers(){
        return this.players;
    }

    public int getModelId(){
        return this.model_id;
    }

    public String getState(){
        return this.state;
    }

}

