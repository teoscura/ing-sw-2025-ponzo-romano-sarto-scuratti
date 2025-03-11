//Done.
package it.polimi.ingsw.model.components;

import it.polimi.ingsw.model.components.exceptions.ConnectorsSizeException;
import it.polimi.ingsw.model.components.visitors.*;
import it.polimi.ingsw.model.player.iSpaceShip;

public abstract class BaseComponent implements iBaseComponent, iVisitable{

    private ConnectorType[] connectors;
    private ComponentRotation rotation;
    private int position;

    protected BaseComponent(ConnectorType[] connectors, 
                            ComponentRotation rotation){
        if(connectors.length!=4){
            throw new ConnectorsSizeException();
        }
        this.connectors = connectors;
        this.rotation = rotation;
    }

    protected BaseComponent(ConnectorType[] connectors, 
                            ComponentRotation rotation,
                            int position){
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

    @Override
    public boolean verify(iSpaceShip state){
        iBaseComponent up = state.getComponent(state.up(position));
        iBaseComponent right = state.getComponent(state.up(position));
        iBaseComponent down = state.getComponent(state.up(position));
        iBaseComponent left = state.getComponent(state.up(position));

        if(up!=null){
            if(!up.getConnector(ComponentRotation.PI).compatible(getConnector(ComponentRotation.ZERO))) return false;
        }
        if(right!=null){
            if(!right.getConnector(ComponentRotation.MINHALFPI).compatible(getConnector(ComponentRotation.POSHALFPI))) return false;
        }
        if(down!=null){
            if(!down.getConnector(ComponentRotation.ZERO).compatible(getConnector(ComponentRotation.PI))) return false;
        }
        if(left!=null){
            if(!left.getConnector(ComponentRotation.POSHALFPI).compatible(getConnector(ComponentRotation.MINHALFPI))) return false;
        }
        return true;
    }

    @Override
    public ConnectorType getConnector(ComponentRotation direction){
        int shift = direction.getShift() + this.rotation.getShift();
        shift = shift % 4;
        return connectors[shift];
    }

    protected int getPosition(){
        return this.position;
    }
    
    //ricordate: non implementare questo metodo, ma va implementato in ogni singola sottoclasse
    // (e' letteralmente la def di abstract, ma fa bene ricordarlo).
    @Override
    abstract public void check(iVisitor v);
    
}
