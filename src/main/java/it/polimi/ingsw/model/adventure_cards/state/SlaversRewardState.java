package it.polimi.ingsw.model.adventure_cards.state;

import java.util.List;

import it.polimi.ingsw.message.exceptions.MessageInvalidException;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.adventure_cards.SlaversCard;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.state.VoyageState;

public class SlaversRewardState extends CardState {

    private final SlaversCard card;
    private final List<Player> list;
    private boolean responded = false;
    private boolean took_reward = false;

    //XXX implement accepted messages;

    public SlaversRewardState(VoyageState state, SlaversCard card, List<Player> list){
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
        if(took_reward){
            this.list.getFirst().giveCredits(this.card.getCredits());
            this.state.getPlanche().movePlayer(this.list.getFirst().getColor(), this.card.getDays());
        }
        this.transition();
    }

    @Override
    protected CardState getNext() {
        return null;
    }
    
}
