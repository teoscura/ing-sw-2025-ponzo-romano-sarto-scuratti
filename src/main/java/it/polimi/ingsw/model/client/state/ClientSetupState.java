package it.polimi.ingsw.model.client.state;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.model.client.ClientUnfinishedGameListEntry;
import it.polimi.ingsw.view.ClientView;

public class ClientSetupState implements ClientModelState {
    
    private String setupper_username;
    private ArrayList<ClientUnfinishedGameListEntry> unfinished_games;

    public ClientSetupState(String setupper_username, List<ClientUnfinishedGameListEntry> unfinished_games){
        if(setupper_username == null || unfinished_games == null || unfinished_games.isEmpty()) throw new NullPointerException();
        this.setupper_username = setupper_username;
        this.unfinished_games = new ArrayList<>(unfinished_games);
    }

    public String getSetupperName(){
        return this.setupper_username;
    }

    public ArrayList<ClientUnfinishedGameListEntry> getUnfinishedList(){
        return this.unfinished_games;
    }

    @Override
    public void sendToView(ClientView view) {
        view.show(this);
    }

}
