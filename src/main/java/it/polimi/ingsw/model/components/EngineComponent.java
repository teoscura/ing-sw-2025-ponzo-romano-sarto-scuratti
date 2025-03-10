package it.polimi.ingsw.model.components;

import it.polimi.ingsw.model.components.exceptions.AlreadyPoweredException;
import it.polimi.ingsw.model.components.exceptions.UnpowerableException;
import it.polimi.ingsw.model.components.visitors.iVisitor;
import it.polimi.ingsw.model.player.iSpaceShip;

public class EngineComponent extends BaseComponent{
    
    private int max_power;
    private boolean powerable;
    private boolean powered = false;

    public EngineComponent(ConnectorType[] components,
                           ComponentRotation rotation,
                           EngineType type)
                           throws Exception {
        super(components, rotation);
        this.max_power = type.getMaxPower();
        this.powerable = type.getPowerable();        
    }

    @Override
    public void check(iVisitor v){
        v.visit(this);
    }

    public void turnOn(){
        if(this.powered) throw new AlreadyPoweredException();
        if(!this.powerable) throw new UnpowerableException();
        this.powered = true;
    }

    public void turnOff(){
        this.powered = false;
    }

    public int getCurrentPower(iSpaceShip state, int position){
        //TODO chiedere a ponzo se e' giusto.
        if(!(state.getComponent(state.down(this.getPosition())) == null)){
            return 0;
        }
        if(this.getRotation() != ComponentRotation.PI){
            return this.getPower()>>1;
        }
        return this.getPower();
    }
            
    private int getPower(){
        return this.max_power;
    }       

}

enum EngineType{
    SINGLE (1, false),
    DOUBLE (2, false);

    private int max_power;
    private boolean powerable; //FIXME CHIEDERE A PONZO

    EngineType(int max_power, boolean powerable){
        this.max_power = max_power;
        this.powerable = powerable;
    }

    public int getMaxPower(){
        return this.max_power;
    }

    public boolean getPowerable(){
        return this.powerable;
    }
}
