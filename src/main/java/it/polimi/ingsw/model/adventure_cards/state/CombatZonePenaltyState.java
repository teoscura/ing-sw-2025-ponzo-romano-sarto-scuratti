package it.polimi.ingsw.model.adventure_cards.state;

import java.util.List;

import it.polimi.ingsw.message.client.ShotMessage;
import it.polimi.ingsw.message.exceptions.MessageInvalidException;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.adventure_cards.utils.CombatZonePenalty;
import it.polimi.ingsw.model.adventure_cards.utils.CombatZoneSection;
import it.polimi.ingsw.model.adventure_cards.utils.ProjectileArray;
import it.polimi.ingsw.model.adventure_cards.visitors.ContainsRemoveVisitor;
import it.polimi.ingsw.model.adventure_cards.visitors.CrewRemoveVisitor;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.VoyageState;

public class CombatZonePenaltyState extends CardState {

    private final List<CombatZoneSection> sections;
    private final ProjectileArray shots;
    private final Player target;
    private boolean responded = false;
    private List<ShipCoords> coords = null;

    //XXX implement allowed messages

    public CombatZonePenaltyState(VoyageState state, List<CombatZoneSection> sections, ProjectileArray shots, Player target){
        super(state);
        if(sections==null||shots==null||target==null);
        this.sections = sections;
        this.shots = shots;
        this.target = target;
    }

    @Override
    public void init() {
        super.init();
        if(sections.getFirst().getPenalty()==CombatZonePenalty.DAYS){
            this.state.getPlanche().movePlayer(this.target.getColor(), -this.sections.getFirst().getAmount());
            this.transition();
        }
        this.target.getDescriptor().sendMessage(new ShotMessage(this.shots.getProjectiles().get(0)));
    }

    @Override
    public void validate(ServerMessage message) throws MessageInvalidException {
        message.receive(this);
        if(!responded) return;
        if(this.sections.getFirst().getPenalty()==CombatZonePenalty.SHOTS){
            this.target.getSpaceShip().handleShot(this.shots.getProjectiles().get(0));
        }
        else if(this.sections.getFirst().getPenalty()==CombatZonePenalty.CARGO){
            ContainsRemoveVisitor v = new ContainsRemoveVisitor();
            for(ShipCoords s : this.coords) this.target.getSpaceShip().getComponent(s).check(v);
        }
        else if(this.sections.getFirst().getPenalty()==CombatZonePenalty.CREW){
            CrewRemoveVisitor v = new CrewRemoveVisitor(target.getSpaceShip());
            for(ShipCoords s : this.coords) this.target.getSpaceShip().getComponent(s).check(v);
        }
        this.transition();
    }

    @Override
    protected CardState getNext() {
        this.shots.getProjectiles().removeFirst();
        if(this.target.getSpaceShip().getBrokeCenter()) return new CombatZoneNewCabinState(state, sections, shots, target);
        if(this.sections.getFirst().getPenalty()==CombatZonePenalty.SHOTS && !this.shots.getProjectiles().isEmpty()){
            return new CombatZonePenaltyState(state, sections, shots, target);
        }
        this.sections.removeFirst();
        if(!this.sections.isEmpty()) return new CombatZoneAnnounceState(state, sections, shots);
        return null;
    }
    
}
