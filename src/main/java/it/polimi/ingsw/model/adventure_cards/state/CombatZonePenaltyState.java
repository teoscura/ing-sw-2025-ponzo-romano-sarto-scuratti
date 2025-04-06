package it.polimi.ingsw.model.adventure_cards.state;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.EmptyMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.adventure_cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.adventure_cards.utils.CombatZonePenalty;
import it.polimi.ingsw.model.adventure_cards.utils.CombatZoneSection;
import it.polimi.ingsw.model.adventure_cards.utils.ProjectileArray;
import it.polimi.ingsw.model.adventure_cards.visitors.ContainsRemoveVisitor;
import it.polimi.ingsw.model.adventure_cards.visitors.CrewRemoveVisitor;
import it.polimi.ingsw.model.components.enums.ShipmentType;
import it.polimi.ingsw.model.components.exceptions.ContainerEmptyException;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.VoyageState;

class CombatZonePenaltyState extends CardState {

    private final List<CombatZoneSection> sections;
    private final ProjectileArray shots;
    private final Player target;
    private final int[] required;
    private boolean responded = false;
    private List<ShipCoords> coords;

    public CombatZonePenaltyState(VoyageState state, List<CombatZoneSection> sections, ProjectileArray shots, Player target){
        super(state);
        if(sections==null||shots==null||target==null);
        this.sections = sections;
        this.shots = shots;
        this.target = target;
        this.coords = new ArrayList<>();
        if(this.sections.getFirst().getPenalty()!=CombatZonePenalty.CARGO){
            this.required = null;
            return;
        }
        this.required = new int[5];
        int penalty = this.sections.getFirst().getAmount();
        int[] player_cargo = this.target.getSpaceShip().getContains();
        for(ShipmentType t : ShipmentType.values()){
            if(t.getValue()==0) break;
            int tmp = penalty - player_cargo[t.getValue()-1];
            if(tmp<=0){
                this.required[t.getValue()-1] = penalty;
                return;
            }
            else{
                this.required[t.getValue()-1] = player_cargo[t.getValue()-1];
                penalty = tmp;
            }
        }
        if(penalty>0){
            this.required[4] = penalty;
        }
    }

    @Override
    public void init() {
        super.init();
        if(sections.getFirst().getPenalty()!=CombatZonePenalty.DAYS) return;
        this.state.getPlanche().movePlayer(state, target, -sections.getFirst().getAmount());
        this.transition();
    }

    @Override
    public void validate(ServerMessage message) throws ForbiddenCallException {
        message.receive(this);
        if(!responded&&!this.target.getRetired()) return;
        if(this.sections.getFirst().getPenalty()==CombatZonePenalty.SHOTS){
            this.target.getSpaceShip().handleShot(this.shots.getProjectiles().get(0));
        }
        else if(this.sections.getFirst().getPenalty()==CombatZonePenalty.CREW){
            CrewRemoveVisitor v = new CrewRemoveVisitor(target.getSpaceShip());
            for(ShipCoords s : this.coords) this.target.getSpaceShip().getComponent(s).check(v);
        }
        this.transition();
    }

    @Override
    protected CardState getNext() {
        if(this.target.getRetired() || this.target.getDisconnected()){
            this.sections.removeFirst();
            if(!this.sections.isEmpty()) return new CombatZoneAnnounceState(state, sections, shots);
            return null;
        }
        if(!target.getSpaceShip().getBrokeCenter()) target.getSpaceShip().verifyAndClean();
        this.shots.getProjectiles().removeFirst();
        if(this.target.getSpaceShip().getBrokeCenter()) return new CombatZoneNewCabinState(state, sections, shots, target);
        if(this.sections.getFirst().getPenalty()==CombatZonePenalty.SHOTS && !this.shots.getProjectiles().isEmpty()){
            return new CombatZonePenaltyState(state, sections, shots, target);
        }
        this.sections.removeFirst();
        if(!this.sections.isEmpty()) return new CombatZoneAnnounceState(state, sections, shots);
        return null;
    }

    @Override 
    public void turnOn(Player p, ShipCoords target_coords, ShipCoords battery_coords){
        if(p!=this.target){
            p.getDescriptor().sendMessage(new ViewMessage("It's not your turn!"));
            return;
        }
        if(this.sections.getFirst().getPenalty()!=CombatZonePenalty.SHOTS){
            p.getDescriptor().sendMessage(new ViewMessage("This penalty doesn't allow this action!"));
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
        if(p!=this.target){
            p.getDescriptor().sendMessage(new ViewMessage("It's not your turn!"));
            return;
        }
        if(this.sections.getFirst().getPenalty()!=CombatZonePenalty.SHOTS){
            p.getDescriptor().sendMessage(new ViewMessage("This penalty doesn't allow this action!"));
            return;
        }
        this.responded = true;
    }
    
    @Override 
    public void removeCrew(Player p, ShipCoords cabin_coords) throws ForbiddenCallException{
        if(p!=this.target){
            p.getDescriptor().sendMessage(new ViewMessage("It's not your turn!"));
            return;
        }
        if(this.sections.getFirst().getPenalty()!=CombatZonePenalty.CREW){
            p.getDescriptor().sendMessage(new ViewMessage("This penalty doesn't allow this action!"));
            return;
        }
        CrewRemoveVisitor v = new CrewRemoveVisitor(p.getSpaceShip());
        try {
            p.getSpaceShip().getComponent(cabin_coords).check(v);
        } catch(IllegalTargetException e){
            p.getDescriptor().sendMessage(new ViewMessage("Sent coords of an empty cabin or not a cabin"));
            return;
        }
        if(p.getSpaceShip().getCrew()[0]==0){
            this.state.loseGame(p);
            this.validate(new EmptyMessage(p.getDescriptor()));
            return;
        }
        this.coords.add(cabin_coords);
        if(coords.size()==this.sections.getFirst().getAmount()){
            this.responded = true;
        }
    }
    
    @Override 
    public void removeCargo(Player p, ShipmentType shipment, ShipCoords storage_coords){
        if(p!=this.target){
            p.getDescriptor().sendMessage(new ViewMessage("It's not your turn!"));
            return;
        }
        if(this.sections.getFirst().getPenalty()!=CombatZonePenalty.CARGO){
            p.getDescriptor().sendMessage(new ViewMessage("This penalty doesn't allow this action!"));
            return;
        }
        for(ShipmentType t : ShipmentType.values()){
            if(t.getValue()==0) break;
            if(this.required[t.getValue()-1] <=0 ) continue;
            ContainsRemoveVisitor v = new ContainsRemoveVisitor(t);
            try {
                p.getSpaceShip().getComponent(storage_coords).check(v);
                this.required[t.getValue()-1]--;
                break;
            } catch (ContainerEmptyException e) {
                p.getDescriptor().sendMessage(new ViewMessage("Give merch up in order of value!"));
                return;
            } catch (IllegalArgumentException e){
                p.getDescriptor().sendMessage(new ViewMessage("Sent invalid coords!"));
                return;
            }
        }
        if(this.required[4]>0){
            ContainsRemoveVisitor v = new ContainsRemoveVisitor();
            try{
                p.getSpaceShip().getComponent(storage_coords).check(v);
            } catch (ContainerEmptyException e){
                p.getDescriptor().sendMessage(new ViewMessage("There are no batteries in the coords!"));
                return;
            } catch (IllegalArgumentException e){
                p.getDescriptor().sendMessage(new ViewMessage("Sent invalid coords!"));
                return;
            }
        }
        else{
            this.responded = true;
        }
    }

    public void disconnect(Player p) throws ForbiddenCallException {
        if(this.target==p){
            this.responded = true;
        }
    }
    
}
