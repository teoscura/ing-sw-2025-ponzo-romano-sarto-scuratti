package it.polimi.ingsw.model.adventure_cards.state;

import java.util.List;

import it.polimi.ingsw.exceptions.PlayerNotFoundException;
import it.polimi.ingsw.message.client.CardMessage;
import it.polimi.ingsw.message.exceptions.MessageInvalidException;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.adventure_cards.AbandonedShipCard;
import it.polimi.ingsw.model.player.Player;

public class AbandonedShipAnnounceState extends CardState {

    private final AbandonedShipCard card;
    private final List<Player> list;
    
    public AbandonedShipAnnounceState(ModelInstance model, AbandonedShipCard card, List<Player> clist) {
        super(model);
        if(clist.size()>this.model.getCount().getNumber()||clist.size()<2||clist==null) throw new IllegalArgumentException("Constructed insatisfyable state");
        if(card==null) throw new NullPointerException();
        this.card = card;
        this.list = clist;
    }

    @Override
    public void init() {
        try {
            this.model.getPlayer(list.getFirst().getColor()).getDescriptor().sendMessage(new CardMessage(this.card.getId()));
        } catch (PlayerNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void validate(ServerMessage message) throws MessageInvalidException {
        if(message.getDescriptor().getPlayer().getColor()!=this.list.getFirst().getColor()) throw new MessageInvalidException();
        ////////////////////////////////// 
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validate'");
    }

    @Override
    protected CardState getNext() {
        if(this.card.getExhausted()) return null;
        this.list.removeFirst();
        return this.list.size() == 0 ? null : new AbandonedShipAnnounceState(model, card, this.list);
    }
    
}
