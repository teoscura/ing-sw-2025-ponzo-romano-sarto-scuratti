package it.polimi.ingsw.model.adventure_cards.state;

import java.util.List;

import it.polimi.ingsw.message.exceptions.MessageInvalidException;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.adventure_cards.utils.CombatZoneSection;
import it.polimi.ingsw.model.adventure_cards.utils.ProjectileArray;
import it.polimi.ingsw.model.adventure_cards.utils.CardOrder;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.state.VoyageState;

public class CombatZoneAnnounceState extends CardState {

    private final List<CombatZoneSection> sections;
    private final ProjectileArray shots;
    private List<Player> awaiting;
    private Player target;

    //XXX implement accepted messages;

    public CombatZoneAnnounceState(VoyageState state, List<CombatZoneSection> sections, ProjectileArray shots){
        super(state);
        if(sections==null||shots==null) throw new NullPointerException();
        this.sections = sections;
        this.shots = shots;
        this.awaiting = this.state.getOrder(CardOrder.NORMAL);
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void validate(ServerMessage message) throws MessageInvalidException {
        message.receive(this);
        if(!awaiting.isEmpty()) return;
        this.target = this.state.findCriteria(this.sections.getFirst().getCriteria());
        this.transition();
    }

    @Override
    protected CardState getNext() {
        return new CombatZonePenaltyState(state, sections, shots, target);
    }
    
}
