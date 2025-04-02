package it.polimi.ingsw.model.adventure_cards.state;

import it.polimi.ingsw.exceptions.PlayerNotFoundException;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.adventure_cards.OpenSpaceCard;
import it.polimi.ingsw.model.adventure_cards.utils.CardOrder;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.state.VoyageState;

public class OpenSpaceState extends CardState {

    private final OpenSpaceCard card;

    public OpenSpaceState(VoyageState state, OpenSpaceCard card){
        super(state);
        if(card==null) throw new NullPointerException();
        this.card = card;
    }

    @Override
    public void init() {
        super.init();
        for(Player p : this.state.getOrder(CardOrder.NORMAL)){
            try {
                card.apply(state, p);
            } catch (PlayerNotFoundException e) {
                e.printStackTrace();
            }
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
