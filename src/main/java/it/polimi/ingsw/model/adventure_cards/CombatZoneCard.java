package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.exceptions.PlayerNotFoundException;
import it.polimi.ingsw.message.client.ClientMessage;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.adventure_cards.utils.*;
import it.polimi.ingsw.model.player.iSpaceShip;

public class CombatZoneCard extends Card{

    private final ProjectileArray shots;
    private final CombatZoneLine[] lines;
    private final ClientMessage[] messages;
    private final CardResponseType expected_after;
    private final CombatZoneLine which;

    public CombatZoneCard(int id, 
                          ProjectileArray shots, 
                          CombatZoneLine[] lines_order, 
                          ClientMessage[] messages, 
                          CombatZoneLine special, 
                          CardResponseType response){
        super(id,0);
        if(shots==null||lines_order==null||messages==null) throw new NullPointerException();
        if(lines_order.length!=messages.length||lines_order.length!=3) throw new IllegalArgumentException("Invalid lenght of arguments, must all be three");
        boolean has_shot = false;
        for(ClientMessage m : messages){
            if(m==null&&has_shot) throw new NullPointerException();
            if(m==null) has_shot=true;
        }
        if(lines_order[0]==lines_order[1]||lines_order[0]==lines_order[2]||lines_order[2]==lines_order[1]) throw new IllegalArgumentException("Duplicate line in lines");
        this.shots = shots;
        this.lines = lines_order;
        this.messages = messages;
        this.which = special;
        this.expected_after = response;
    } 

    public CombatZoneLine[] getCriteriaOrder(){
        return this.lines;
    }

    //0 least cannon, 1 least engine, 2 least crew
    @Override
    public ClientMessage apply(ModelInstance model, iSpaceShip ship, CombatZoneLine criteria) throws PlayerNotFoundException {
        if(ship==null || response == null) throw new NullPointerException();
        boolean broken_center_cabin = false;
        switch(criteria){
            case LEAST_ENGINE: {
                if(this.messages[0]==null){
                    for(Projectile p : this.shots.getProjectiles()){
                        broken_center_cabin = ship.handleShot(p);
                    }
                    if(broken_center_cabin) return new BrokenCabinMessage();
                    return null;
                }
                if(this.which.getNumber()==0) this.after_response=this.expected_after;
                return this.messages[0];
            }
            case LEAST_CREW: {
                if(this.messages[1]==null){
                    for(Projectile p : this.shots.getProjectiles()){
                        broken_center_cabin = ship.handleShot(p);
                    }
                    if(broken_center_cabin) return new BrokenCabinMessage();
                    return null;
                }
                if(this.which.getNumber()==1) this.after_response=this.expected_after;
                return this.messages[1];
            }
            case LEAST_CANNON: {
                if(this.messages[2]==null){
                    for(Projectile p : this.shots.getProjectiles()){
                        broken_center_cabin = ship.handleShot(p);
                    }
                    if(broken_center_cabin) return new BrokenCabinMessage();
                    return null;
                }
                if(this.which.getNumber()==2) this.after_response=this.expected_after;
                return this.messages[2];
            }
            default: return null;
        }
    }

}
