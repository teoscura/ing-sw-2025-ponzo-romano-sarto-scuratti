//Done.
package it.polimi.ingsw.controller.match;

import it.polimi.ingsw.model.rulesets.GameModeType;

public class MatchSettings {
    //TODO.
    private GameModeType mode;
    private PlayerCount n_players;

    public MatchSettings(GameModeType mode, PlayerCount n_players){
        
        this.mode = mode;
        this.n_players = n_players;
    }

    public GameModeType getMode(){
        return this.mode;
    }

    public PlayerCount getPlayerNumber(){
        return this.n_players;
    }
}
