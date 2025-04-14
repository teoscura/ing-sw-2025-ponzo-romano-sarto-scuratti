package it.polimi.ingsw.model.client.state;

import java.util.List;

import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.client.player.ClientWaitingPlayer;
import it.polimi.ingsw.view.ClientView;

public class ClientWaitingRoomState implements ClientModelState {

    private final GameModeType type;
    private final List<ClientWaitingPlayer> playerlist;

    public ClientWaitingRoomState(GameModeType type, List<ClientWaitingPlayer> playerlist) {
        if(playerlist==null) throw new NullPointerException();
        this.playerlist = playerlist;
        this.type = type;
    }

    public List<ClientWaitingPlayer> getPlayerList(){
        return this.playerlist;
    }

    @Override
    public void sendToView(ClientView view) {
        view.show(this);
    }
    
}
