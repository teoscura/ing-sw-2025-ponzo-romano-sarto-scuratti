package it.polimi.ingsw.model.client.state;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.model.client.ClientGameListEntry;
import it.polimi.ingsw.view.ClientView;

public class ClientLobbySelectState implements ClientState {

    private ArrayList<ClientGameListEntry> games;

    public ClientLobbySelectState(List<ClientGameListEntry> unfinished_games){
        if(games == null) throw new NullPointerException();
        this.games = new ArrayList<>(games);
    }

    public ArrayList<ClientGameListEntry> getLobbyList(){
        return this.games;
    }

    @Override
    public void sendToView(ClientView view) {
        view.show(this);
    }
    
}
