package it.polimi.ingsw.model.components;

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

    //TODO Exceptions
    public void turnOn() throws Exception {
        if(this.powered) throw new Exception();
        this.powered = true;
    }

    public void turnOff(){
        this.powered = false;
    }

    public ShieldType getShield(){
        return this.type;
    }
}
