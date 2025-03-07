package it.polimi.ingsw.model.components;

enum AlienType{
    HUMAN,
    BROWN,
    PURPLE,
    BOTH, //Cabina collegata sia a support viola che marrone
}

public class CabinComponent extends BaseComponent{
    
    private int crew_number = 0;

    public CabinComponent(ConnectorType[] connectors, 
                          ComponentRotation rotation)
                          throws Exception {
        super(connectors, rotation);
    }

    @Override
    public void check(iVisitor v){
        v.visit(this);
    }

    public int getCrew(){
        //TODO
        return crew_number;
    }

    //TODO: setters for crew with error handling. extend to alien
}
