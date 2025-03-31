//Done.
package it.polimi.ingsw.model.components;

import it.polimi.ingsw.model.components.enums.CannonType;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import it.polimi.ingsw.model.components.exceptions.AlreadyPoweredException;
import it.polimi.ingsw.model.components.exceptions.ComponentNotEmptyException;
import it.polimi.ingsw.model.components.exceptions.UnpowerableException;
import it.polimi.ingsw.model.components.visitors.iVisitor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.player.iSpaceShip;

public class CannonComponent extends BaseComponent{
    
    private int max_power;
    private boolean powered = false;
    private boolean powerable = false;

    public CannonComponent(int id, 
                           ConnectorType[] components,
                           ComponentRotation rotation,
                           CannonType type){
        super(id, components, rotation);
        if(components[0]!=ConnectorType.EMPTY) throw new ComponentNotEmptyException("Top of cannon must be empty!");
        this.max_power = type.getMaxPower();
        this.powerable = type.getPowerable();
    }

    public CannonComponent(int id, 
                           ConnectorType[] components,
                           ComponentRotation rotation,
                           CannonType type,
                           ShipCoords coords){
        super(id, components, rotation, coords);
        if(components[0]!=ConnectorType.EMPTY) throw new ComponentNotEmptyException("Top of cannon must be empty!");
        this.max_power = type.getMaxPower();
        this.powerable = type.getPowerable();       
    }

    @Override
    public void check(iVisitor v){
        v.visit(this);
    }

    @Override
    public boolean verify(iSpaceShip ship){
        ComponentRotation r = this.getRotation();
        iBaseComponent tmp = null; 
        switch(r.getShift()){
            case 0: {
                tmp = ship.getComponent(this.coords.up());
                break;
            }
            case 1: {
                tmp = ship.getComponent(this.coords.right());
                break;
            }
            case 2: {
                tmp = ship.getComponent(this.coords.down());
                break;
            }
            case 3: {
                tmp = ship.getComponent(this.coords.left());
            }
        }
        return tmp == ship.getEmpty() && super.verify(ship);
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
        if(this.getRotation() != ComponentRotation.U000){
            return this.getPower()>>1;
        }
        return this.getPower();
    }

    private int getPower(){
        if(max_power==2 && !this.powered){
            return 0;
        }
        return max_power;
    } 

    @Override
    public boolean powerable(){
        return true;
    }

    @Override
    public void onCreation(iSpaceShip ship){
        if(powerable) ship.addPowerableCoords(this.coords);
    }

    @Override
    public void onDelete(iSpaceShip ship){
        if(powerable) ship.delPowerableCoords(this.coords);
    }
}
