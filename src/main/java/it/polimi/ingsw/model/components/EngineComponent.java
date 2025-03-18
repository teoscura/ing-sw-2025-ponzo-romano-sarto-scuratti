//Done.
package it.polimi.ingsw.model.components;

import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import it.polimi.ingsw.model.components.exceptions.AlreadyPoweredException;
import it.polimi.ingsw.model.components.exceptions.UnpowerableException;
import it.polimi.ingsw.model.components.visitors.iVisitor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.player.iSpaceShip;

public class EngineComponent extends BaseComponent{
    
    private int max_power;
    private boolean powerable;
    private boolean powered = false;

    public EngineComponent(ConnectorType[] components,
                           ComponentRotation rotation,
                           EngineType type){
        super(components, rotation);
        this.max_power = type.getMaxPower();
        this.powerable = type.getPowerable();        
    }

    public EngineComponent(ConnectorType[] components,
                           ComponentRotation rotation,
                           EngineType type,
                           ShipCoords coords){
        super(components, rotation, coords);
        this.max_power = type.getMaxPower();
        this.powerable = type.getPowerable();        
    }

    @Override
    public boolean verify(iSpaceShip state){
        //FreeSpaceVisitor v = new FreeSpaceVisitor();
        iBaseComponent tmp = state.getComponent(this.coords.down());
        //tmp.check(v);
        //if(this.getRotation()!=ComponentRotation.U000) return false;
        //if(v.getSpaceIsFree()) return true;
        return tmp==state.getEmpty();
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

    public int getCurrentPower(){
        if(this.getRotation() != ComponentRotation.U180){
            return this.getPower()>>1;  //Divide by two.
        }
        return this.getPower();
    }
            
    private int getPower(){
        if(powerable && !powered){
            return 0;
        }
        return this.max_power;
    } 
    
    @Override
    public boolean powerable(){
        return true;
    }
}

enum EngineType{
    SINGLE (1, false),
    DOUBLE (2, true );

    private int max_power;
    private boolean powerable;

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
