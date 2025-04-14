package it.polimi.ingsw.model.adventure_cards.state;

import java.util.List;

import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.adventure_cards.StardustCard;
import it.polimi.ingsw.model.adventure_cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.adventure_cards.utils.CardOrder;
import it.polimi.ingsw.model.client.card.ClientAwaitConfirmCardStateDecorator;
import it.polimi.ingsw.model.client.card.ClientBaseCardState;
import it.polimi.ingsw.model.client.card.ClientCardState;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.state.VoyageState;

public class StardustState extends CardState {

    private final StardustCard card;
    private List<Player> awaiting = null;

    public StardustState(VoyageState state, StardustCard card){
        super(state);
        if(card==null) throw new NullPointerException();
        this.card = card;
    }

    @Override
    public void init() {
        super.init();
        for(Player p : this.state.getOrder(CardOrder.INVERSE)){
            card.apply(this.state, p);
        }
        this.awaiting = state.getOrder(CardOrder.NORMAL);
    }
    
    @Override
    public void validate(ServerMessage message) throws ForbiddenCallException {
        message.receive(this);
        if(!awaiting.isEmpty()){
            this.sendNotify();
            return;
        }
        this.transition();
    }

    @Override
    public ClientCardState getClientCardState(){
        List<PlayerColor> tmp = this.awaiting.stream().map(p -> p.getColor()).toList();
        return new ClientAwaitConfirmCardStateDecorator(
            new ClientBaseCardState(card.getId()),
            tmp);
    }

    @Override
    public void progressTurn(Player p){
        if(!this.awaiting.contains(p)){
            p.getDescriptor().sendMessage(new ViewMessage("You already confirmed your actions, can't do anything else"));
            return;
        }
        this.awaiting.remove(p);
    }

    @Override
    protected CardState getNext() {
        return null;
    }
    
}
