package it.polimi.ingsw.model.client.state;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.model.client.ClientGameListEntry;
import it.polimi.ingsw.view.ClientView;

public class ClientSetupState implements ClientState {
    
    private String setupper_username;
    private ArrayList<ClientGameListEntry> unfinished_games;

    public ClientSetupState(String setupper_username, List<ClientGameListEntry> unfinished_games){
        if(setupper_username == null || unfinished_games == null) throw new NullPointerException();
        this.setupper_username = setupper_username;
        this.unfinished_games = new ArrayList<>(unfinished_games);
    }

    public String getSetupperName(){
        return this.setupper_username;
    }

    public ArrayList<ClientGameListEntry> getUnfinishedList(){
        return this.unfinished_games;
    }

    @Override
    public void sendToView(ClientView view) {
        view.show(this);
    }

}
