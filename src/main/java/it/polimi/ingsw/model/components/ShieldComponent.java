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
    
    private ShieldType type = ShieldType.NE;
    private boolean powered = false;

    public ShieldComponent(ConnectorType[] connectors, 
                           ComponentRotation rotation,
                           ShieldType type){
        super(connectors, rotation);
        if(type == ShieldType.NONE) throw new IllegalArgumentException();
        this.type = type;
    }

    public ShieldComponent(ConnectorType[] connectors, 
                           ComponentRotation rotation,
                           ShieldType type,
                           ShipCoords coords){
        super(connectors, rotation, coords);
        if(type == ShieldType.NONE) throw new IllegalArgumentException();
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
        return this.type;
    }
    
}
