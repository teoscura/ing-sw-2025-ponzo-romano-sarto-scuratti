package it.polimi.ingsw.model.client.state;

import java.util.List;

import it.polimi.ingsw.model.client.ClientPlayer;
import it.polimi.ingsw.view.ClientView;

public class ClientConstructionState extends ClientModelState {

    protected ClientConstructionState(List<ClientPlayer> playerlist) {
        super(playerlist);
        xx
    }

    @Override
    public void sendToView(ClientView view) {
        view.show(this);
    }

}
