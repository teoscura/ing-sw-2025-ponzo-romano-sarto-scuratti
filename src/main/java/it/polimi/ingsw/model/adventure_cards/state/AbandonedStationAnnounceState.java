package it.polimi.ingsw.model.adventure_cards.state;

import java.util.List;

import it.polimi.ingsw.message.exceptions.MessageInvalidException;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.adventure_cards.AbandonedStationCard;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.state.VoyageState;

public class AbandonedStationAnnounceState extends CardState {

    private final AbandonedStationCard card;
    private final List<Player> list;
    private boolean responded = false;
    private int id = -1;

    public AbandonedStationAnnounceState(VoyageState state, AbandonedStationCard card, List<Player> clist) {
        super(state);
        if(clist.size()>this.state.getCount().getNumber()||clist.size()<2||clist==null) throw new IllegalArgumentException("Constructed insatisfyable state");
        if(card==null) throw new NullPointerException();
        this.card = card;
        this.list = clist;
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void validate(ServerMessage message) throws MessageInvalidException {
        message.receive(this);
        if(!responded) return;
        this.card.apply(state, this.list.getFirst(), id);
        this.transition();
    }

    @Override
    protected CardState getNext() {
        if(this.card.getExhausted()) return new AbandonedStationRewardState(state, this.list.getFirst(), this.card.getPlanet());
        this.list.removeFirst();
        return this.list.size() == 0 ? null : new AbandonedStationAnnounceState(state, card, this.list);
    }
    
}
