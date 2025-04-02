package it.polimi.ingsw.model.adventure_cards.state;

import java.util.List;

import it.polimi.ingsw.message.exceptions.MessageInvalidException;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.adventure_cards.SmugglersCard;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.state.VoyageState;

public class SmugglersAnnounceState extends CardState {

    private final SmugglersCard card;
    private final List<Player> list;
    private boolean responded = false;
    private boolean result = false;

    //XXX implement accepted messages;

    public SmugglersAnnounceState(VoyageState state, SmugglersCard card, List<Player> list){
        super(state);
        if(list.size()>this.state.getCount().getNumber()||list.size()<1||list==null) throw new IllegalArgumentException("Constructed insatisfyable state");
        if(card==null) throw new NullPointerException();
        this.card = card;
        this.list = list;
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void validate(ServerMessage message) throws MessageInvalidException {
        message.receive(this);
        if(!responded) return;
        result = this.card.apply(list.getFirst());
        this.transition();
    }

    @Override
    protected CardState getNext() {
        if(this.card.getExhausted()) return new SmugglersRewardState(state, card, list);
        if(!result) return new SmugglersLoseState(state, card, list);
        this.list.removeFirst();
        if(!list.isEmpty()) return new SmugglersAnnounceState(state, card, list);
        return null;
    }
    
}
