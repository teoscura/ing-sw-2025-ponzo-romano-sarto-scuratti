package it.polimi.ingsw.controller.match.state;

import it.polimi.ingsw.controller.match.MatchController;
import it.polimi.ingsw.model.rulesets.GameModeType;

public class VoyageState extends GameState {

    private int turn = 0;

    protected VoyageState(MatchController match, GameModeType type) {
        super(match, type);
        //TODO
    }

    @Override
    public void action() {
        //pick card.
        //show card
        //wait for accept/input/timer.
        //apply/
        //increase
    }

    @Override
    public boolean finished() {
        return this.turn == this.type.getNumCards();
    }

    @Override
    public GameState getNext() {
        return new LeaderboardState();
    }
    
}
