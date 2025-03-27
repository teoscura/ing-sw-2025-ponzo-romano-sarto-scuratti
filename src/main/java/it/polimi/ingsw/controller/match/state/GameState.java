package it.polimi.ingsw.controller.match.state;

import it.polimi.ingsw.controller.match.MatchController;
import it.polimi.ingsw.model.rulesets.GameModeType;

public abstract class GameState {

    protected final MatchController match;
    protected final ModelInstance model;
    protected final GameModeType type;
    
    protected GameState(MatchController match, ModelInstance model, GameModeType type){
        if(match==null||type==null||model==null) throw new NullPointerException();
        this.match = match;
        this.model = model;
        this.type = type;
    }

    public abstract void action();

    public abstract boolean finished();

    public abstract GameState getNext();

}
