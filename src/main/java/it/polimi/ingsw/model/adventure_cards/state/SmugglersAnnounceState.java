package it.polimi.ingsw.model.adventure_cards.state;

import it.polimi.ingsw.message.exceptions.MessageInvalidException;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.adventure_cards.SmugglersCard;

public class SmugglersAnnounceState extends CardState {

    private final SmugglersCard card;

    public SmugglersAnnounceState(ModelInstance model, SmugglersCard card){
        super(model);
        if(card==null) throw new NullPointerException();
        this.card = card;
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
