package it.polimi.ingsw.model.adventure_cards.state;

import java.util.List;

import it.polimi.ingsw.exceptions.PlayerNotFoundException;
import it.polimi.ingsw.message.client.CardMessage;
import it.polimi.ingsw.message.exceptions.MessageInvalidException;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.adventure_cards.AbandonedShipCard;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.state.VoyageState;

public class AbandonedShipAnnounceState extends CardState {

    private final AbandonedShipCard card;
    private final List<Player> list;
    private boolean responded = false;
    private int id = -1;
    
    public AbandonedShipAnnounceState(VoyageState state, AbandonedShipCard card, List<Player> list) {
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
        this.card.apply(state, this.list.getFirst(), id);
        this.transition();
    }

    @Override
    protected CardState getNext() {
        if(this.card.getExhausted()) return new AbandonedShipRewardState(state, card, this.list);
        this.list.removeFirst();
        return this.list.size() == 0 ? null : new AbandonedShipAnnounceState(state, card, this.list);
    }
    
}
