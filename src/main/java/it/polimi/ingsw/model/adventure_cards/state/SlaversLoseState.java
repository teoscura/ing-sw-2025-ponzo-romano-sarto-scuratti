package it.polimi.ingsw.model.adventure_cards.state;

import it.polimi.ingsw.message.exceptions.MessageInvalidException;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.state.VoyageState;

public class SlaversLoseState extends CardState {

    protected SlaversLoseState(VoyageState state, SlaversCard card, List<Player> list) {
        super(state);
        //TODO Auto-generated constructor stub
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'init'");
    }

    @Override
    public void validate(ServerMessage message) throws MessageInvalidException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validate'");
    }

    @Override
    protected CardState getNext() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getNext'");
    }
    
}
