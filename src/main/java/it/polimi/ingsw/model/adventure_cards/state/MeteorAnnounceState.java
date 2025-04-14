package it.polimi.ingsw.model.adventure_cards.state;

import java.util.List;

import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.adventure_cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.adventure_cards.utils.CardOrder;
import it.polimi.ingsw.model.adventure_cards.utils.ProjectileArray;
import it.polimi.ingsw.model.client.card.ClientAwaitConfirmCardStateDecorator;
import it.polimi.ingsw.model.client.card.ClientBaseCardState;
import it.polimi.ingsw.model.client.card.ClientCardState;
import it.polimi.ingsw.model.client.card.ClientMeteoriteCardStateDecorator;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.VoyageState;

public class MeteorAnnounceState extends CardState {

    private final int card_id;
    private final ProjectileArray left;
    private final List<Player> awaiting;
    private boolean broke_cabin;

    public MeteorAnnounceState(VoyageState state, int card_id, ProjectileArray array){
        super(state);
        if(array==null) throw new NullPointerException();
        if(card_id<1||card_id>120||(card_id<100&&1>20)) throw new IllegalArgumentException();
        this.card_id = card_id; 
        this.left = array;
        this.awaiting = this.state.getOrder(CardOrder.NORMAL);
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void validate(ServerMessage message) throws ForbiddenCallException {
        message.receive(this);
        if(!awaiting.isEmpty()){
            this.sendNotify();
            return;
        }
        for(Player p : this.state.getOrder(CardOrder.NORMAL)){
            this.broke_cabin = p.getSpaceShip().handleMeteorite(this.left.getProjectiles().getFirst());
        }
        this.transition();
    }

    @Override
    public ClientCardState getClientCardState(){
        List<PlayerColor> tmp = this.awaiting.stream().map(p->p.getColor()).toList();
        return new ClientMeteoriteCardStateDecorator(
            new ClientAwaitConfirmCardStateDecorator(
                new ClientBaseCardState(this.card_id), 
                tmp),
            this.left.getProjectiles().getFirst());
    }

    @Override
    protected CardState getNext(){
        for(Player p : this.state.getOrder(CardOrder.NORMAL)){
            if(!p.getSpaceShip().getBrokeCenter()) p.getSpaceShip().verifyAndClean();
        }
        if(broke_cabin) return new MeteorNewCabinState(state, card_id, left);
        this.left.getProjectiles().removeFirst();
        if(!this.left.getProjectiles().isEmpty()) return new MeteorAnnounceState(state, card_id, left);
        return null;
    }

    @Override
    public void turnOn(Player p, ShipCoords target_coords, ShipCoords battery_coords){
        if(!this.awaiting.contains(p)){
            p.getDescriptor().sendMessage(new ViewMessage("You already confirmed your actions, can't do anything else"));
            return;
        }
        try{
            p.getSpaceShip().turnOn(target_coords, battery_coords);
        } catch (IllegalTargetException e){
            p.getDescriptor().sendMessage(new ViewMessage("Coords are not valid for the turnOn operation!"));
            return;
        }
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
    public void disconnect(Player p) throws ForbiddenCallException {
        if(this.awaiting.contains(p)){
            this.awaiting.remove(p);
            return;
        }
    }

}
