package it.polimi.ingsw.model.adventure_cards.state;

import java.util.List;

import it.polimi.ingsw.exceptions.PlayerNotFoundException;
import it.polimi.ingsw.message.client.CardMessage;
import it.polimi.ingsw.message.exceptions.MessageInvalidException;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.adventure_cards.SlaversCard;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.state.VoyageState;

public class SlaversAnnounceState extends CardState {

    private final SlaversCard card;
    private final List<Player> list;
    private boolean responded = false;
    private boolean result = false;

    //XXX handle allowed messages

    public SlaversAnnounceState(VoyageState state, SlaversCard card, List<Player> list){
        super(state);
        if(list.size()>this.state.getCount().getNumber()||list.size()<2||list==null) throw new IllegalArgumentException("Constructed insatisfyable state");
        if(card==null) throw new NullPointerException();
        this.card = card;
        this.list = list;
    }


    @Override
    public void init() {
        super.init();
        try {
            this.state.getPlayer(list.getFirst().getColor()).getDescriptor().sendMessage(new CardMessage(this.card.getId()));
        } catch (PlayerNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void validate(ServerMessage message) throws MessageInvalidException {
        message.receive(this);
        if(!responded) return;
        this.result = this.card.apply(state, this.list.getFirst());
        this.transition();
    }

    @Override
    protected CardState getNext() {
        if(result && this.card.getExhausted()) return new SlaversRewardState();
        if(!result) return new SlaversLoseState();
        this.list.removeFirst();
        if(list.size()!=0) return new SlaversAnnounceState(state, card, list);
        return null;
    }
    
}
