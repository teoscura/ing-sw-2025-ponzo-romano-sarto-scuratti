package it.polimi.ingsw.model.adventure_cards.state;

import java.util.List;

import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.adventure_cards.PiratesCard;
import it.polimi.ingsw.model.adventure_cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.state.VoyageState;

class PiratesRewardState extends CardState {
    
    private final PiratesCard card;
    private final List<Player> list;
    private boolean responded = false;
    private boolean took_reward = false;

    public PiratesRewardState(VoyageState state, PiratesCard card, List<Player> list){
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
    public void validate(ServerMessage message) throws ForbiddenCallException {
        message.receive(this);
        if(!responded) return;
        if(took_reward){
            this.list.getFirst().giveCredits(card.getCredits());
            this.state.getPlanche().movePlayer(state, list.getFirst(), card.getDays());
        }
        this.transition();
    }

    @Override
    protected CardState getNext() {
        return null;
    }

    @Override
    public void setTakeReward(Player p, boolean take){
        if(p!=this.list.getFirst()){
            p.getDescriptor().sendMessage(new ViewMessage("It's not your turn!"));
            return;
        }
        this.took_reward = take;
        this.responded = true;
    }

    public void disconnect(Player p) throws ForbiddenCallException {
        p.getDescriptor().sendMessage(new ViewMessage("This state doesn't support this function!"));
        throw new ForbiddenCallException("This state doesn't support this function.");
        XXX
    }

}
