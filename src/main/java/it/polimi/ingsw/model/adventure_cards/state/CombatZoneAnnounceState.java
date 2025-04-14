package it.polimi.ingsw.model.adventure_cards.state;

import java.util.List;

import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.adventure_cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.adventure_cards.utils.CardOrder;
import it.polimi.ingsw.model.adventure_cards.utils.CombatZoneSection;
import it.polimi.ingsw.model.adventure_cards.utils.ProjectileArray;
import it.polimi.ingsw.model.client.card.ClientAwaitConfirmCardStateDecorator;
import it.polimi.ingsw.model.client.card.ClientBaseCardState;
import it.polimi.ingsw.model.client.card.ClientCardState;
import it.polimi.ingsw.model.client.card.ClientCombatZoneIndexCardStateDecorator;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.VoyageState;

public class CombatZoneAnnounceState extends CardState {

    private final int card_id;
    private final List<CombatZoneSection> sections;
    private final ProjectileArray shots;
    private List<Player> awaiting;
    private Player target;
    
    public CombatZoneAnnounceState(VoyageState state, int card_id, List<CombatZoneSection> sections, ProjectileArray shots){
        super(state);
        if(sections==null||shots==null) throw new NullPointerException();
        if(card_id<1||card_id>120||(card_id<100&&1>20)) throw new IllegalArgumentException();
        this.card_id = card_id; 
        this.sections = sections;
        this.shots = shots;
        this.awaiting = this.state.getOrder(CardOrder.NORMAL);
    }

    @Override
    public void init() {
        super.init();
        if(this.state.getOrder(CardOrder.NORMAL).size()<=1) this.transition();
    }

    @Override
    public void validate(ServerMessage message) throws ForbiddenCallException {
        message.receive(this);
        if(!awaiting.isEmpty()){
            this.sendNotify();
            return;
        }
        this.target = this.state.findCriteria(this.sections.getFirst().getCriteria());
        this.transition();
    }

    @Override
    public ClientCardState getClientCardState(){
        return new ClientCombatZoneIndexCardStateDecorator(
                    new ClientAwaitConfirmCardStateDecorator(
                        new ClientBaseCardState(card_id),
                        this.awaiting.stream().map(p -> p.getColor()).toList()),
                        3 - this.sections.size());
    }

    @Override
    protected CardState getNext() {
        if(this.state.getOrder(CardOrder.NORMAL).size()<=1) return null;
        return new CombatZonePenaltyState(state, card_id, sections, shots, target);
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
        }
    }
    
}
