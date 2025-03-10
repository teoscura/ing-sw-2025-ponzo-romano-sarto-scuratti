package it.polimi.ingsw.model.components;

import it.polimi.ingsw.model.components.exceptions.AlreadyPoweredException;
import it.polimi.ingsw.model.components.exceptions.UnpowerableException;
import it.polimi.ingsw.model.components.visitors.iVisitor;
import it.polimi.ingsw.model.player.iSpaceShip;

public class CannonComponent extends BaseComponent{
    
    private int max_power;
    private boolean powered = false;
    private boolean powerable = false;

    public CannonComponent(ConnectorType[] components,
                           ComponentRotation rotation,
                           CannonType type){
        super(components, rotation);
        this.max_power = type.getMaxPower();
        this.powerable = type.getPowerable();
    }

    public CannonComponent(ConnectorType[] components,
                           ComponentRotation rotation,
                           CannonType type,
                           int position){
        super(components, rotation, position);
        this.max_power = type.getMaxPower();
        this.powerable = type.getPowerable();       
    }

    @Override
    public void check(iVisitor v){
        v.visit(this);
    }

    @Override
    public boolean verify(iSpaceShip state){
        //TODO
        return false;
    }

    public void turnOn(){
        if(this.powered) throw new AlreadyPoweredException();
        if(this.powerable) throw new UnpowerableException();
        this.powered = true;
    }

    public void turnOff(){
        this.powered = false;
    }


    //TODO chiedere a ponzo.
    public int getCurrentPower(iSpaceShip state, int position){
        if(this.getRotation() != ComponentRotation.ZERO){
            return this.getPower()>>1;
        }
        return this.getPower();
    }

    //TODO IDEM
    private int getPower(){
        if(max_power==2 && !this.powered){
            return 0;
        }
        return max_power == 2 ? 2 : 1;
    } 
}

enum CannonType{
    SINGLE (1, false),
    DOUBLE (2, false);

    private int max_power;
    private boolean powerable;

    CannonType(int max_power, boolean powerable){
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
