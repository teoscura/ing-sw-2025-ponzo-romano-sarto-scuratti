package it.polimi.ingsw.controller.match.state;

import it.polimi.ingsw.controller.match.MatchController;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.rulesets.GameModeType;

public class LeaderboardState extends GameState {

    protected LeaderboardState(MatchController match, ModelInstance model, GameModeType type) {
        super(match, model, type);
        //TODO Auto-generated constructor stub
    }

    @Override
    public void action() {
        // TODO send everyone leaderboard screen. auto disconnect from server.
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
