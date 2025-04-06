package it.polimi.ingsw.model.state;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.adventure_cards.exceptions.ForbiddenCallException;

public class WaitingState extends GameState {

    //XXX finish implementing
    private final List<ClientDescriptor> connected;
    private final PlayerCount count;

    public WaitingState(ModelInstance model, GameModeType type, PlayerCount count) {
        super(model, type, count, null);
        this.connected = new ArrayList<>();
        this.count = count;
    }

    @Override
    public void init(){
        super.init();
    }

    @Override
    public void validate(ServerMessage message) throws ForbiddenCallException {
        message.receive(this);
        if(this.connected.size()<count.getNumber()) return;
        
        this.transition();
    }

    @Override
    public GameState getNext() {
        return new ConstructionState(model, type, count, players);
    }

    public void connect(ClientDescriptor client) throws ForbiddenCallException {
        //XXX
    }

    public void disconnect(ClientDescriptor client) throws ForbiddenCallException {
        //XXX
    }
    

}
