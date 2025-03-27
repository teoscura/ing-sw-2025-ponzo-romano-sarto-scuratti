package it.polimi.ingsw.controller.match.state;

import it.polimi.ingsw.controller.match.MatchController;
import it.polimi.ingsw.model.rulesets.GameModeType;

public class ConstructionState extends GameState{

    protected ConstructionState(MatchController match, GameModeType type) {
        super(match, type);
        //TODO
    }

    @Override
    public void action() {
        //.Send everyone leaderboard.
    }

    @Override
    public boolean finished() {
        return true;
    }

    @Override
    public GameState getNext() {
        return null;
    }
    
}
