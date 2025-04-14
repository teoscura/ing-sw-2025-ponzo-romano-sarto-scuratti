package it.polimi.ingsw.model.client.state;

import java.util.List;

import it.polimi.ingsw.model.client.player.ClientEndgamePlayer;
import it.polimi.ingsw.view.ClientView;

public class ClientEndgameState implements ClientModelState {

    private final List<ClientEndgamePlayer> playerlist;

    public ClientEndgameState(List<ClientEndgamePlayer> playerlist) {
        this.playerlist = playerlist;
    }

    public List<ClientEndgamePlayer> getPlayerList(){
        return this.playerlist;
    }

    @Override
    public void sendToView(ClientView view) {
        view.show(this);
    }
    
}
