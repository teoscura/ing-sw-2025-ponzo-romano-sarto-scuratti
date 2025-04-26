//Done.
package it.polimi.ingsw.model.components;

import it.polimi.ingsw.model.client.components.ClientBaseComponent;
import it.polimi.ingsw.model.client.components.ClientComponent;
import it.polimi.ingsw.model.client.components.ClientPoweredComponentDecorator;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import it.polimi.ingsw.model.components.enums.EngineType;
import it.polimi.ingsw.model.components.exceptions.AlreadyPoweredException;
import it.polimi.ingsw.model.components.exceptions.ComponentNotEmptyException;
import it.polimi.ingsw.model.components.exceptions.UnpowerableException;
import it.polimi.ingsw.model.components.visitors.iVisitor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.player.iSpaceShip;

public class EngineComponent extends BaseComponent {
    
    private final int max_power;
    private final boolean powerable;
    private boolean powered = false;

    public EngineComponent(int id, 
                           ConnectorType[] components,
                           ComponentRotation rotation,
                           EngineType type){
        super(id, components, rotation);
        if(components[2]!=ConnectorType.EMPTY) throw new ComponentNotEmptyException("Bottom of engine must be empty!");
        this.max_power = type.getMaxPower();
        this.powerable = type.getPowerable();        
    }

    public EngineComponent(int id, 
                           ConnectorType[] components,
                           ComponentRotation rotation,
                           EngineType type,
                           ShipCoords coords){
        super(id, components, rotation, coords);
        if(components[2]!=ConnectorType.EMPTY) throw new ComponentNotEmptyException("Bottom of engine must be empty!");
        this.max_power = type.getMaxPower();
        this.powerable = type.getPowerable();        
    }

    @Override
    public boolean verify(iSpaceShip ship){
        if(this.getRotation()!=ComponentRotation.U000||ship.getComponent(this.coords.down())!=ship.getEmpty()) return false; 
        return super.verify(ship);
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
        if(this.getRotation() != ComponentRotation.U000){
            return 0;
        }
        return this.getPower();
    }
            
    private int getPower(){
        if(this.powerable) return this.powered ? max_power : 0;
        return this.max_power;
    } 
    
    @Override
    public boolean powerable(){
        return true;
    } //redundant

    @Override
    public void onCreation(iSpaceShip ship, ShipCoords coords){
        this.coords = coords;
        if(powerable) ship.addPowerableCoords(this.coords);
    }

    @Override
    public void onDelete(iSpaceShip ship){
        if(powerable) ship.delPowerableCoords(this.coords);
    }

    @Override
    public ClientComponent getClientComponent() {
        return new ClientPoweredComponentDecorator(new ClientBaseComponent(getID(), getRotation()), powered);
    }

}
