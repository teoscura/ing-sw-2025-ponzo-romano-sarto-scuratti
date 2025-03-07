package it.polimi.ingsw.model.components;

public class CabinComponent extends BaseComponent{
    
    private int crew_number = 0;

    public CabinComponent(ConnectorType[] connectors, 
                          ComponentRotation rotation)
                          throws Exception {
        super(connectors, rotation);
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
