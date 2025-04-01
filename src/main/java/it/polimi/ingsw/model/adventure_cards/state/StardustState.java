package it.polimi.ingsw.model.adventure_cards.state;

import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.adventure_cards.StardustCard;
import it.polimi.ingsw.model.adventure_cards.utils.CardOrder;
import it.polimi.ingsw.model.player.Player;

public class StardustState extends CardState {

    private final StardustCard card;

    public StardustState(ModelInstance model, StardustCard card){
        super(model);
        if(card==null) throw new NullPointerException();
        this.card = card;
    }

    @Override
    public void init() {
        for(Player p : this.model.getOrder(CardOrder.INVERSE)){
            card.apply(this.model, p);
        }
        this.transition();
    }

    @Override
    public void validate(ServerMessage message) {
        return;
    }

    @Override
    protected CardState getNext() {
        return null;
    }
    
}
