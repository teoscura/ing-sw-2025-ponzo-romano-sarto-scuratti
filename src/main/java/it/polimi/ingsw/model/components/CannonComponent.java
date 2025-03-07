package it.polimi.ingsw.model.components;

enum CannonType{
    SINGLE,
    DOUBLE
}

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
        //TODO check function
    }

    public void turnOn(){
        this.powered = true;
    }

    public void turnOff(){
        this.powered = false;
    }

    public int getCurrentPower(iSpaceShip state, int position){
        //TODO power function check under itself.
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
