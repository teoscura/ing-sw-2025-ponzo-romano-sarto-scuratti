package it.polimi.ingsw.model.client.state;

import java.util.List;

import it.polimi.ingsw.model.client.ClientPlayer;
import it.polimi.ingsw.view.ClientView;

public abstract class ClientModelState {
    
    private final List<ClientPlayer> playerlist;

    protected ClientModelState(List<ClientPlayer> playerlist){
        this.playerlist = playerlist;
    }

    protected List<ClientPlayer> getPlayerList(){
        return this.playerlist;
    }

    public abstract void sendToView(ClientView view);

}
