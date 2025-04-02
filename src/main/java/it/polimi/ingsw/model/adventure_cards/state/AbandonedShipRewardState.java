package it.polimi.ingsw.model.adventure_cards.state;

import java.util.List;

import it.polimi.ingsw.exceptions.PlayerNotFoundException;
import it.polimi.ingsw.message.client.CargoMessage;
import it.polimi.ingsw.message.exceptions.MessageInvalidException;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.adventure_cards.utils.Planet;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.state.VoyageState;

public class AbandonedShipRewardState extends CardState {
    
    private final Planet reward;
    private final List<Player> list;
    private boolean responded;

    //XXX implement allowed messages.

    public AbandonedShipRewardState(VoyageState state, List<Player> list, Planet planet) {
        super(state);
        if(state==null||list==null||planet==null) throw new IllegalArgumentException("Constructed insatisfyable state");
        this.list = list;
        this.reward = planet;
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void validate(ServerMessage message) throws MessageInvalidException {
        message.receive(this);
        if(!responded) return;
        //XXX HANDLE CARGO MOVEMENTS
        this.state.getPlanche().movePlayer(this.list.getFirst().getColor(), this.card.getDays());
        this.transition();
    }

    @Override
    protected CardState getNext() {
        return null;
    }
}
