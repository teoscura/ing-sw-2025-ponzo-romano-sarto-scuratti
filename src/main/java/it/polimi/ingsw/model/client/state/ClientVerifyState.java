package it.polimi.ingsw.model.client.state;

import java.util.ArrayList;

import it.polimi.ingsw.model.client.player.ClientVerifyPlayer;
import it.polimi.ingsw.view.ClientView;

public class ClientVerifyState implements ClientModelState {

    private final ArrayList<ClientVerifyPlayer> players;

    public ClientVerifyState(ArrayList<ClientVerifyPlayer> playerlist) {
        if(playerlist==null) throw new NullPointerException();
        if(playerlist.size()>4) throw new IllegalArgumentException();
        this.players = playerlist;
    }

    public ArrayList<ClientVerifyPlayer> getPlayers() {
        return players;
    }

    @Override
    public void sendToView(ClientView view) {
        view.show(this);
    }
    
}
