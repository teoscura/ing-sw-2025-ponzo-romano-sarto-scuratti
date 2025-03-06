package it.polimi.ingsw.model.components;

public class CabinComponent extends BaseComponent{
    
    private int crew_number = 0;

    public CabinComponent(ConnectorType[] connectors, ComponentRotation rotation){
        super(connectors, rotation);
    }

    @Override
    public ConnectorType[] getConnectors(){
        // TODO
        return null;
    }

    @Override
    public ComponentRotation getRotation(){
        //TODO
        return null;
    }

    @Override
    public boolean verify(iSpaceShip state, int position){
        //TODO
        return false;
    }

    @Override
    public void check(iVisitor v){
        //TODO
    }

    public int getCrew(){
        //TODO
        return crew_number;
    }

    //TODO: setters for crew with error handling.
}
