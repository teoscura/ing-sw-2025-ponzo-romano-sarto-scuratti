package it.polimi.ingsw.model.adventure_cards.state;

import java.util.List;

import it.polimi.ingsw.exceptions.PlayerNotFoundException;
import it.polimi.ingsw.message.client.CardMessage;
import it.polimi.ingsw.message.exceptions.MessageInvalidException;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.adventure_cards.AbandonedStationCard;
import it.polimi.ingsw.model.player.Player;

public class AbandonedStationAnnounceState extends CardState {

    private final AbandonedStationCard card;
    private final List<Player> list;

    public AbandonedStationAnnounceState(ModelInstance model, AbandonedStationCard card, List<Player> clist) {
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validate'");
    }

    @Override
    protected CardState getNext() {
        if(this.card.getExhausted()) return new AbandonedStationRewardState(model, this.list.getFirst(), this.card.getPlanet());
        this.list.removeFirst();
        return this.list.size() == 0 ? null : new AbandonedStationAnnounceState(model, card, this.list);
    }
    
}
