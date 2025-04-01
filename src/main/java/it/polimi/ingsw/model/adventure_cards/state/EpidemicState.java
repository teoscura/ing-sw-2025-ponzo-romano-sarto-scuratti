package it.polimi.ingsw.model.adventure_cards.state;

import it.polimi.ingsw.exceptions.PlayerNotFoundException;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.adventure_cards.EpidemicCard;
import it.polimi.ingsw.model.adventure_cards.utils.CardOrder;
import it.polimi.ingsw.model.player.Player;

public class EpidemicState extends CardState{
    
    private final EpidemicCard card;

    public EpidemicState(ModelInstance model, EpidemicCard card){
        super(model);
        if(card==null) throw new NullPointerException();
        this.card = card;
    }

    @Override
    public void init() {
        for(Player p : this.model.getOrder(CardOrder.INVERSE)){
            try {
                card.apply(this.model, p);
            } catch (PlayerNotFoundException e) {
                e.printStackTrace();
            }
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
