package it.polimi.ingsw.model.adventure_cards.state;

import java.util.List;

import it.polimi.ingsw.exceptions.PlayerNotFoundException;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.adventure_cards.EpidemicCard;
import it.polimi.ingsw.model.adventure_cards.utils.CardOrder;
import it.polimi.ingsw.model.client.card.ClientAwaitConfirmCardStateDecorator;
import it.polimi.ingsw.model.client.card.ClientBaseCardState;
import it.polimi.ingsw.model.client.card.ClientCardState;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.state.VoyageState;

public class EpidemicState extends CardState{
    
    private final EpidemicCard card;
    private List<Player> awaiting;

    public EpidemicState(VoyageState state, EpidemicCard card){
        super(state);
        if(card==null) throw new NullPointerException();
        this.card = card;
    }

    @Override
    public void init() {
        super.init();
        for(Player p : this.state.getOrder(CardOrder.INVERSE)){
            try {
                card.apply(this.state, p);
                if(p.getSpaceShip().getCrew()[0]==0) this.state.loseGame(p);
            } catch (PlayerNotFoundException e) {
                e.printStackTrace();
            }
        }
        this.awaiting = state.getOrder(CardOrder.NORMAL);
    }

    @Override
    public void validate(ServerMessage message) {
        AsDASDASD
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
