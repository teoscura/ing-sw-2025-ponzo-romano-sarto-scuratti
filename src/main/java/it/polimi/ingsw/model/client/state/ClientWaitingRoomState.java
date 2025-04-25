package it.polimi.ingsw.model.client.state;

import java.util.ArrayList;

import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.client.player.ClientWaitingPlayer;
import it.polimi.ingsw.view.ClientView;

public class ClientWaitingRoomState implements ClientModelState {

    private final GameModeType type;
    private final ArrayList<ClientWaitingPlayer> playerlist;

    public ClientWaitingRoomState(GameModeType type, ArrayList<ClientWaitingPlayer> playerlist) {
        if(playerlist==null) throw new NullPointerException();
        this.playerlist = playerlist;
        this.type = type;
    }

    public GameModeType getType(){
        return this.type;
    }

    public ArrayList<ClientWaitingPlayer> getPlayerList(){
        return this.playerlist;
    }

    @Override
    public void sendToView(ClientView view) {
        view.show(this);
    }
    
}
