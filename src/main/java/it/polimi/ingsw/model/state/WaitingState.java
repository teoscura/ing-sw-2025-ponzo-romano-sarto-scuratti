package it.polimi.ingsw.model.state;

import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.PlayerCount;

public class WaitingState extends GameState {
    
    public WaitingState(ModelInstance model, GameModeType type, PlayerCount count) {
        super(model, type, count, null);
    }

    // @Override
    // public JsonState serialize(){
    //     return null;
    // }

    @Override
    public GameState getNext() {
        // TODO Auto-generated method stub
        return null;
    }

}
