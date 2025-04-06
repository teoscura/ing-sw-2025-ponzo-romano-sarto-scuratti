package it.polimi.ingsw.model.client.state;

import java.util.List;

import it.polimi.ingsw.model.client.ClientPlayer;
import it.polimi.ingsw.view.ClientView;

public class ClientWaitingRoomState extends ClientModelState {

    protected ClientWaitingRoomState(List<ClientPlayer> playerlist) {
        super(playerlist);
    }

    @Override
    public void sendToView(ClientView view) {
        view.show(this);
    }
    
}
