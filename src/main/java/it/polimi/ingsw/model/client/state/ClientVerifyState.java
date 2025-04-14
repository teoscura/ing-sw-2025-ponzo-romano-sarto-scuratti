package it.polimi.ingsw.model.client.state;

import java.util.List;

import it.polimi.ingsw.model.client.ClientSpaceShip;
import it.polimi.ingsw.model.client.player.ClientVerifyPlayer;
import it.polimi.ingsw.model.player.VerifyResult;
import it.polimi.ingsw.view.ClientView;

public class ClientVerifyState implements ClientModelState {

    private final List<ClientVerifyPlayer> players;

    public ClientVerifyState(ClientSpaceShip ship, VerifyResult[][] results, List<ClientVerifyPlayer> playerlist) {
        if(playerlist==null) throw new NullPointerException();
        if(playerlist.size()>4) throw new IllegalArgumentException();
        this.players = playerlist;
    }

    public List<ClientVerifyPlayer> getPlayers() {
        return players;
    }

    @Override
    public void sendToView(ClientView view) {
        view.show(this);
    }
    
}
