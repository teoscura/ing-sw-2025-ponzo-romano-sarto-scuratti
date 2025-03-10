package it.polimi.ingsw.model.components;

import it.polimi.ingsw.model.components.exceptions.ConnectorsSizeException;
import it.polimi.ingsw.model.components.visitors.*;
import it.polimi.ingsw.model.player.iSpaceShip;

public abstract class BaseComponent implements iBaseComponent, iVisitable{
    
    private ConnectorType[] connectors;
    private ComponentRotation rotation;
    private int position;

    protected BaseComponent(ConnectorType[] connectors, 
                            ComponentRotation rotation) 
                            throws Exception{
        if(connectors.length!=4){
            throw new ConnectorsSizeException();
        }
        this.connectors = connectors;
        this.rotation = rotation;
    }

    protected BaseComponent(ConnectorType[] connectors, 
                            ComponentRotation rotation,
                            int position) 
                            throws Exception{
        if(connectors.length!=4){
            throw new ConnectorsSizeException();
        }
        this.connectors = connectors;
        this.rotation = rotation;
        this.position = position;
    }

    @Override
    public ConnectorType[] getConnectors(){
        return connectors;
    }

    @Override
    public ComponentRotation getRotation(){
        return rotation;
    }

    public boolean verify(iSpaceShip state, int position){
        //TODO
        return false;
    }

    protected int getPosition(){
        return this.position;
    }

    //ricordate: non implementare questo metodo, ma va implementato in ogni singola sottoclasse
    // (e' letteralmente la def di abstract, ma fa bene ricordarlo).
    @Override
    abstract public void check(iVisitor v);
    
}
