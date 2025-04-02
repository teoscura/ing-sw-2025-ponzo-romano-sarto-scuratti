package it.polimi.ingsw.model.adventure_cards.state;

import java.util.List;

import it.polimi.ingsw.message.exceptions.MessageInvalidException;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.adventure_cards.PiratesCard;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.state.VoyageState;

public class PiratesAnnounceState extends CardState {

    private final PiratesCard card;
    private final List<Player> list;
    private boolean responded = false;

    //XXX implement allowed messages

    public PiratesAnnounceState(VoyageState state, PiratesCard card, List<Player> list){
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
        this.card.apply(this.state, this.list.getFirst());
    }

    @Override
    protected CardState getNext() {
        if(this.card.getExhausted()) return new PiratesRewardState(state, card, list);
        if(this.list.getFirst().getSpaceShip().getBrokeCenter()) return new PiratesNewCabinState(state, card, list);
        this.list.removeFirst();
        if(!this.list.isEmpty()) return new PiratesAnnounceState(state, card, list);
        return null;
    }
    
}
