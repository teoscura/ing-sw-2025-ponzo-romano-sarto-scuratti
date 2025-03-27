package it.polimi.ingsw.controller.match.state;

import it.polimi.ingsw.controller.match.MatchController;
import it.polimi.ingsw.controller.match.PlayerCount;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.rulesets.GameModeType;
import it.polimi.ingsw.server.ClientDescriptor;

public class WaitingRoomState extends GameState {

    private ClientDescriptor[] connected;

    public WaitingRoomState(MatchController match,
                            ModelInstance model, 
                            GameModeType type, 
                            PlayerCount count,
                            ClientDescriptor host){
        super(match, model, type);
        //XXX finish.
    }

    @Override
    public void action() {
        //Broadcast to every player if someone connected.
    }

    @Override
    public boolean finished() {
        return connected.length == this.model.getCount().getNumber();
    }

    @Override
    public GameState getNext() {
        return new ConstructionState(match, model, type);
    }
    
}
