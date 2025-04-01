package it.polimi.ingsw.model.adventure_cards.state;

import it.polimi.ingsw.message.exceptions.MessageInvalidException;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.ModelInstance;

public abstract class CardState {
    
    protected final ModelInstance model;

    protected CardState(ModelInstance model){
        if(model==null) throw new NullPointerException();
        this.model = model;
    }

    public abstract void init();

    public abstract void validate(ServerMessage message) throws MessageInvalidException;

    protected abstract CardState getNext();

    public void transition(){
        if(this.getNext()==null) this.model.setCardState(null);
        this.model.setCardState(this.getNext());
    }

}
