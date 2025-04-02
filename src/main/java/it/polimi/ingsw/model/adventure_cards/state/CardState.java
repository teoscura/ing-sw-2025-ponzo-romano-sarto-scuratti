package it.polimi.ingsw.model.adventure_cards.state;

import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.exceptions.MessageInvalidException;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.adventure_cards.utils.CardOrder;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.state.VoyageState;

public abstract class CardState {

    //XXX add empty handles for each possible message that gets here.
    
    protected final VoyageState state;

    protected CardState(VoyageState state){
        if(state==null) throw new NullPointerException();
        this.state = state;
    }

    public void init(){
        for(Player p : state.getOrder(CardOrder.NORMAL)){
            p.getDescriptor().sendMessage(new NotifyStateUpdateMessage(this.state));
        }
    };

    public abstract void validate(ServerMessage message) throws MessageInvalidException;

    protected abstract CardState getNext();

    public void transition(){
        this.state.setCardState(this.getNext());
    }

}
