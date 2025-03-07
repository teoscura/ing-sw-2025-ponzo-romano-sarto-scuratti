package it.polimi.ingsw.model.components;

enum EngineType{
    SINGLE,
    DOUBLE,
}

public class EngineComponent extends BaseComponent{
    
    private EngineType type;
    private boolean powered = false;

    public EngineComponent(ConnectorType[] components,
                           ComponentRotation rotation,
                           EngineType type)
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
        if(this.getRotation() != ComponentRotation.PI){
            return this.getPower()>>1;
        }
        return this.getPower();
    }
            
    private int getPower(){
        if(this.type == EngineType.DOUBLE && !this.powered){
            return 0;
        }
        return this.type == EngineType.DOUBLE ? 2 : 1;
    }       

}
