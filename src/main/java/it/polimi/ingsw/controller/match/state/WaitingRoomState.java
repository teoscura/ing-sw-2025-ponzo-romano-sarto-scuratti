package it.polimi.ingsw.controller.match.state;


import java.util.ArrayList;

import it.polimi.ingsw.controller.match.MatchController;
import it.polimi.ingsw.controller.match.PlayerCount;
import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.model.rulesets.GameModeType;

public class WaitingRoomState extends GameState {


    public WaitingRoomState(MatchController match, 
                            GameModeType type, 
                            PlayerCount count,
                            ClientDescriptor host){
        super(match, type);
        //XXX finish.
    }

    @Override
    public void action() {
        //Broadcast to every player if someone connected.
    }

    @Override
    public boolean finished() {
        return players == count.getNumber();
    }

    @Override
    public GameState getNext() {
        return new ConstructionState();
    }
    
}
