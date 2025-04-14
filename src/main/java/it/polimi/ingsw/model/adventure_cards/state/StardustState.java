package it.polimi.ingsw.model.adventure_cards.state;

import java.util.List;

import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.adventure_cards.StardustCard;
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
    public void validate(ServerMessage message) {
        ASDASDASDASD
    }

    @Override
    public ClientCardState getClientCardState(){
        List<PlayerColor> tmp = this.awaiting.stream().map(p -> p.getColor()).toList();
        return new ClientAwaitConfirmCardStateDecorator(
            new ClientBaseCardState(card.getId()),
            tmp);
    }

    aaaaa aggiungi continue mannaggia a sorrt;

    @Override
    protected CardState getNext() {
        return null;
    }
    
}
