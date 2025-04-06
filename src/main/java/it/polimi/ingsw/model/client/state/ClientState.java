package it.polimi.ingsw.model.client.state;

import java.util.List;

import it.polimi.ingsw.model.client.ClientPlayer;
import it.polimi.ingsw.view.ClientView;

public abstract class ClientState {
    
    private final List<ClientPlayer> playerlist;

    protected ClientState(List<ClientPlayer> playerlist){
        this.playerlist = playerlist;
    }

    protected List<ClientPlayer> getPlayerList(){
        return this.playerlist;
    }

    public abstract void sendToView(ClientView view);

}
