package it.polimi.ingsw.model.components;

import it.polimi.ingsw.model.components.exceptions.AlreadyPoweredException;
import it.polimi.ingsw.model.components.visitors.iVisitor;

enum ShieldType{
    NE,
    SE,
    SW,
    NW,
}

public class ShieldComponent extends BaseComponent {
    
    private ShieldType type = ShieldType.NE;
    private boolean powered = false;

    public ShieldComponent(ConnectorType[] connectors, 
                           ComponentRotation rotation)
                           throws Exception {
        super(connectors, rotation);
    }

    @Override
    public void check(iVisitor v){
        v.visit(this);
    }

    public void turnOn() throws Exception {
        if(this.powered) throw new AlreadyPoweredException();
        this.powered = true;
    }

    public void turnOff(){
        this.powered = false;
    }

    public boolean getPowered(){
        return this.powered;
    }

    public ShieldType getShield(){
        return this.type;
    }
}
