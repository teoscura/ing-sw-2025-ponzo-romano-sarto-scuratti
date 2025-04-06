package it.polimi.ingsw.model.client.state;

import java.util.List;

import it.polimi.ingsw.model.client.ClientPlayer;
import it.polimi.ingsw.view.ClientView;

public class ClientEndgameState extends ClientState {

    protected ClientEndgameState(List<ClientPlayer> playerlist) {
        super(playerlist);
        xx
    }

    @Override
    public void sendToView(ClientView view) {
        view.show(this);
    }
    
}
