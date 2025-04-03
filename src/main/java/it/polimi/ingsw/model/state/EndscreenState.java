package it.polimi.ingsw.model.state;

import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.player.Player;

public class EndscreenState extends GameState {

    public EndscreenState(ModelInstance model, GameModeType type, PlayerCount count, Player[] players) {
        super(model, type, count, players);
    }

    //XXX finish implementing

    // @Override
    // public JsonState serialize(){
    //     return null;
    // }

    @Override
    public GameState getNext() {
        return null;
    }

}
