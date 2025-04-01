package it.polimi.ingsw.model.adventure_cards.state;

import it.polimi.ingsw.message.client.CardMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.adventure_cards.iCard;
import it.polimi.ingsw.model.adventure_cards.utils.CardOrder;
import it.polimi.ingsw.model.player.Player;

public class OpenSpaceState extends CardState {

    private final iCard card;

    public OpenSpaceState(ModelInstance model, iCard card){
        super(model);
        if(card==null) throw new NullPointerException();
        this.card = card;
    }

    @Override
    public void init() {
        //Send everyone card, and idk let them reply;
        for(Player p : this.model.getOrder(CardOrder.NORMAL)){
            p.getDescriptor().sendMessage(new CardMessage(this.card.getId()));
        }
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
