package it.polimi.ingsw.model.adventure_cards.state;

import java.util.List;

import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.adventure_cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.adventure_cards.utils.CombatZoneSection;
import it.polimi.ingsw.model.adventure_cards.utils.ProjectileArray;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.VoyageState;

class CombatZoneNewCabinState extends CardState {
    
    private final List<CombatZoneSection> sections;
    private final ProjectileArray shots;
    private final Player target;

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
    public void validate(ServerMessage message)throws ForbiddenCallException{
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

    @Override
    public void setNewShipCenter(Player p, ShipCoords new_center){
        if(p!=this.target){
            p.getDescriptor().sendMessage(new ViewMessage("It's not your turn!"));
            return;
        }
        try{
            p.getSpaceShip().setCenter(new_center);
        } catch (IllegalTargetException e){
            p.getDescriptor().sendMessage(new ViewMessage("Target is an empty space!"));
        } catch (ForbiddenCallException e){
            //Should never get here.
            p.getDescriptor().sendMessage(new ViewMessage("Cabin isn't broken!"));
        }
    }

    public void disconnect(Player p) throws ForbiddenCallException {
        p.getDescriptor().sendMessage(new ViewMessage("This state doesn't support this function!"));
        throw new ForbiddenCallException("This state doesn't support this function.");
        XXX
    }

}
