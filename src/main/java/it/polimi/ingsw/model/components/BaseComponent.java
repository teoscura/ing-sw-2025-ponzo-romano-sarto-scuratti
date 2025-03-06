package it.polimi.ingsw.model.components;

public abstract class BaseComponent implements iBaseComponent {
    
    private ConnectorType[] connectors;

    private ComponentRotation rotation;

    protected BaseComponent(ConnectorType[] connectors, ComponentRotation rotation) throws Exception{
        if(connectors.length!=4){
            //TODO create error type
            throw new Exception();
        }
        this.connectors = connectors;
        this.rotation = rotation;
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
    
}
