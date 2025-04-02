package it.polimi.ingsw.model.adventure_cards.state;

import java.util.Arrays;

import it.polimi.ingsw.message.client.MeteorMessage;
import it.polimi.ingsw.message.exceptions.MessageInvalidException;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.adventure_cards.utils.CardOrder;
import it.polimi.ingsw.model.adventure_cards.utils.Projectile;
import it.polimi.ingsw.model.adventure_cards.utils.ProjectileArray;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.state.VoyageState;

public class MeteorAnnounceState extends CardState {

    private final ProjectileArray left;
    private final boolean[] responded;
    private boolean broke_cabin;

    //XXX todo: add handling of allowed messages.

    public MeteorAnnounceState(VoyageState state, ProjectileArray array){
        super(state);
        if(array==null) throw new NullPointerException();
        this.left = array;
        this.responded = new boolean[state.getCount().getNumber()];
        Arrays.fill(this.responded, false);
    }

    @Override
    public void init() {
        super.init();
        for(Player p : this.state.getOrder(CardOrder.NORMAL)){
            p.getDescriptor().sendMessage(new MeteorMessage(this.left.getProjectiles().getFirst()));
        }
    }

    @Override
    public void validate(ServerMessage message) throws MessageInvalidException {
        message.receive(this);
        boolean missing = false;
        for(boolean b : responded){
            missing = b || missing;
        }
        if(missing) return;
        for(Player p : this.state.getOrder(CardOrder.NORMAL)){
            this.broke_cabin = p.getSpaceShip().handleMeteorite(this.left.getProjectiles().getFirst());
        }
        this.transition();
    }

    @Override
    protected CardState getNext(){
        if(broke_cabin) return new MeteorNewCabinState(state, left);
        this.left.getProjectiles().removeFirst();
        //This is a memory safe workaround on the lackluster generics support that collections have.
        if(this.left.getProjectiles().size()>0) return new MeteorAnnounceState(state, new ProjectileArray((Projectile[])this.left.getProjectiles().toArray(new Projectile[0])));
        return null;
    }
    
}
