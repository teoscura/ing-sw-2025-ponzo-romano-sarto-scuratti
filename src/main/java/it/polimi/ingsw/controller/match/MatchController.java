package it.polimi.ingsw.controller.match;

import it.polimi.ingsw.exceptions.NegativeArgumentException;
import it.polimi.ingsw.message.Message;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.rulesets.GameModeType;

public class MatchController {
    //TODO.
    private long match_id;
    private boolean waiting = true;

    public MatchController(long match_id, GameModeType type, PlayerCount count){
        if(match_id<0) throw new NegativeArgumentException("Negative match id is forbidden");
        
    }

    public void recieveMessage(PlayerColor color, Message message){
        
    }

    public void onClosure(){
        //Serialize to JSON.
    }

}
