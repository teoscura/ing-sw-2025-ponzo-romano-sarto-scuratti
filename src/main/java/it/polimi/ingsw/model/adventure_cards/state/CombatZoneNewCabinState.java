package it.polimi.ingsw.model.adventure_cards.state;

import java.util.List;

import it.polimi.ingsw.message.exceptions.MessageInvalidException;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.adventure_cards.utils.CombatZoneSection;
import it.polimi.ingsw.model.adventure_cards.utils.ProjectileArray;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.state.VoyageState;

public class CombatZoneNewCabinState extends CardState {
    
    private final List<CombatZoneSection> sections;
    private final ProjectileArray shots;
    private final Player target;

    //XXX implement accepted messages;

    public CombatZoneNewCabinState(VoyageState state, List<CombatZoneSection> sections, ProjectileArray shots, Player target){
        super(state);
        if(sections==null||shots==null||target==null);
        this.sections = sections;
        this.shots = shots;
        this.target = target;
    }

    @Override
    public void init(){
        super.init();
    }

    @Override
    public void validate(ServerMessage message) throws MessageInvalidException {
        message.receive(this);
        if(target.getSpaceShip().getBrokeCenter()) return;
        this.transition();
    }

    @Override
    protected CardState getNext() {
        if(!this.shots.getProjectiles().isEmpty()) return new CombatZonePenaltyState(state, sections, shots, target);
        this.sections.removeFirst();
        if(!this.sections.isEmpty()) return new CombatZoneAnnounceState(state, sections, shots);
        return null;
    }

}
