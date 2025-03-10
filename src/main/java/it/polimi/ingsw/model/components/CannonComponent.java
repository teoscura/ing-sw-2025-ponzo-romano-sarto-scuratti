package it.polimi.ingsw.model.components;

import it.polimi.ingsw.model.components.exceptions.AlreadyPoweredException;
import it.polimi.ingsw.model.components.visitors.iVisitor;
import it.polimi.ingsw.model.player.iSpaceShip;

enum CannonType{
    SINGLE,
    DOUBLE
}


//FIXME chiedere a ponzo meccaniche effettive di come funziona. e se si modificare come engine.
public class CannonComponent extends BaseComponent{
    
    private CannonType type;
    private boolean powered = false;

    public CannonComponent(ConnectorType[] components,
                           ComponentRotation rotation,
                           CannonType type)
                           throws Exception {
        super(components, rotation);
        this.type = type;        
    }

    @Override
    public void check(iVisitor v){
        v.visit(this);
    }

    public void turnOn(){
        if(this.powered) throw new AlreadyPoweredException();
        this.powered = true;
    }

    public void turnOff(){
        this.powered = false;
    }

    public int getCurrentPower(iSpaceShip state, int position){
        //FIXME chiedere a ponzo 
        if(!(state.getComponent(state.up(this.getPosition())) == null)){
            return 0;
        }
        if(this.getRotation() != ComponentRotation.ZERO){
            return this.getPower()>>1;
        }
        return this.getPower();
    }
            
    private int getPower(){
        if(this.type == CannonType.DOUBLE && !this.powered){
            return 0;
        }
        return this.type == CannonType.DOUBLE ? 2 : 1;
    } 
}
