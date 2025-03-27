package it.polimi.ingsw.controller.match.state;

import it.polimi.ingsw.controller.match.MatchController;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.rulesets.GameModeType;

public class CorrectionState extends GameState {

    protected CorrectionState(MatchController match, ModelInstance model, GameModeType type) {
            super(match, model, type);
            //TODO 
    }
    
        @Override
    public void action() {
        //TODO foreach player, check for changes, if changes happened, rerun verify and if verify passed then
        //send confirmation. if not, send again and let them modify.
    }

    @Override
    public boolean finished() {
        //TODO Logic based on the gamemode type
        return false;
    }

    @Override
    public GameState getNext() {
        return new VoyageState(this.match, this.model, this.type);
    }

}
