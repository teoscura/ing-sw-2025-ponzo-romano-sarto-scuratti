//Done.
package it.polimi.ingsw.model.components;

import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import it.polimi.ingsw.model.components.enums.ShieldType;
import it.polimi.ingsw.model.components.exceptions.AlreadyPoweredException;
import it.polimi.ingsw.model.components.visitors.iVisitor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.player.iSpaceShip;

public class ShieldComponent extends BaseComponent {
    
    private boolean powered = false;

    public ShieldComponent(ConnectorType[] connectors, 
                           ComponentRotation rotation){
        super(connectors, rotation);
    }

    public ShieldComponent(ConnectorType[] connectors, 
                           ComponentRotation rotation,
                           ShipCoords coords){
        super(connectors, rotation, coords);
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

    public boolean getPowered(){
        return this.powered;
    }
    @Override
    public boolean powerable(){
        return true;
    }

    @Override
    public void onCreation(iSpaceShip ship){
        ship.addPowerableCoords(this.coords);
    }

    @Override
    public void onDelete(iSpaceShip ship){
        ship.delPowerableCoords(this.coords);
    }

    public ShieldType getShield(){
        if(!this.powered) return ShieldType.NONE;
        switch(this.getRotation().getShift()){
            case 0: return ShieldType.NE;
            case 1: return ShieldType.SE;
            case 2: return ShieldType.SW;
            case 3: return ShieldType.NW;
        }
        return ShieldType.NE;
    }
    
}
