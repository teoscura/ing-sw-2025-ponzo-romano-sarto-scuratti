package it.polimi.ingsw.model.adventure_cards.state;

import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.adventure_cards.utils.CardOrder;
import it.polimi.ingsw.model.adventure_cards.utils.ProjectileArray;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.state.VoyageState;

public class MeteorNewCabinState extends CardState {

    private final ProjectileArray left;

    //XXX Handle allowed messages

    public MeteorNewCabinState(VoyageState state, ProjectileArray left){
        super(state);
        if(left==null) throw new NullPointerException();
        this.left = left;
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void validate(ServerMessage message) {
        message.receive(this);
        boolean missing = false;
        for(Player p : this.state.getOrder(CardOrder.NORMAL)){
            missing = missing || p.getSpaceShip().getBrokeCenter();
        }
        if(missing) return;
        this.transition();
    }

    @Override
    protected CardState getNext() {
        this.left.getProjectiles().removeFirst();
        if(!this.left.getProjectiles().isEmpty()) return new MeteorAnnounceState(state, left);
        return null;
    }
    
}
