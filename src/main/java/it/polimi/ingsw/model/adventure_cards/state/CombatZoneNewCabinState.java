package it.polimi.ingsw.model.adventure_cards.state;

import java.util.Arrays;
import java.util.List;

import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.adventure_cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.adventure_cards.utils.CombatZoneSection;
import it.polimi.ingsw.model.adventure_cards.utils.ProjectileArray;
import it.polimi.ingsw.model.client.card.ClientBaseCardState;
import it.polimi.ingsw.model.client.card.ClientCardState;
import it.polimi.ingsw.model.client.card.ClientCombatZoneIndexCardStateDecorator;
import it.polimi.ingsw.model.client.card.ClientNewCenterCardStateDecorator;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.VoyageState;

class CombatZoneNewCabinState extends CardState {
    
    private final int card_id;
    private final List<CombatZoneSection> sections;
    private final ProjectileArray shots;
    private final Player target;

    public CombatZoneNewCabinState(VoyageState state, int card_id, List<CombatZoneSection> sections, ProjectileArray shots, Player target){
        super(state);
        if(sections==null||shots==null||target==null);
        if(card_id<1||card_id>120||(card_id<100&&1>20)) throw new IllegalArgumentException();
        this.card_id = card_id;
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
        if(target.getSpaceShip().getBrokeCenter()){
            this.sendNotify();
            return;
        }
        this.transition();
    }

    @Override
    public ClientCardState getClientCardState(){
        List<PlayerColor> awaiting = Arrays.asList(new PlayerColor[]{target.getColor()});
        return new ClientNewCenterCardStateDecorator(
            new ClientCombatZoneIndexCardStateDecorator(
                new ClientBaseCardState(this.card_id), 3 - this.sections.size()), 
                awaiting);
    }

    @Override
    protected CardState getNext() {
        if(this.target.getRetired()){
            this.sections.removeFirst();
            if(!this.sections.isEmpty()) return new CombatZoneAnnounceState(state, card_id, sections, shots);
            return null;
        }
        if(!this.shots.getProjectiles().isEmpty()) return new CombatZonePenaltyState(state, card_id, sections, shots, target);
        this.sections.removeFirst();
        if(!this.sections.isEmpty()) return new CombatZoneAnnounceState(state, card_id, sections, shots);
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

    @Override
    public void disconnect(Player p) throws ForbiddenCallException {
        if(target==p){
            this.state.loseGame(p);
            this.transition();
        }
        //XXX controllare se va bene.
    }

}
