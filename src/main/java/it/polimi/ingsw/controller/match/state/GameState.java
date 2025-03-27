package it.polimi.ingsw.controller.match.state;

import it.polimi.ingsw.controller.match.MatchController;
import it.polimi.ingsw.controller.message.iGameMessageHandler;
import it.polimi.ingsw.model.rulesets.GameModeType;

public abstract class GameState implements iGameMessageHandler {

    private final MatchController match;
    private final GameModeType type;
    
    protected GameState(MatchController match, GameModeType type){
        if(match==null||type==null) throw new NullPointerException();
        this.match = match;
        this.type = type;
    }

    public abstract boolean finished();

    public abstract GameState getNext();

}
