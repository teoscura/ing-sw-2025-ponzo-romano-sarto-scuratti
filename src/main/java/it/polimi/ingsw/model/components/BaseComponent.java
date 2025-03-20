//Done.
package it.polimi.ingsw.model.components;

import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import it.polimi.ingsw.model.components.exceptions.ConnectorsSizeException;
import it.polimi.ingsw.model.components.visitors.*;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.player.iSpaceShip;

public abstract class BaseComponent implements iBaseComponent, iVisitable{

    private final ConnectorType[] connectors;
    private final ComponentRotation rotation;
    protected ShipCoords coords;

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
                            ShipCoords coords){
        if(connectors.length!=4){
            throw new ConnectorsSizeException();
        }
        this.connectors = connectors;
        this.rotation = rotation;
        this.coords = coords;
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
    public boolean verify(iSpaceShip ship){
        iBaseComponent up = ship.getComponent(this.coords.up());
        iBaseComponent right = ship.getComponent(this.coords.right());
        iBaseComponent down = ship.getComponent(this.coords.down());
        iBaseComponent left = ship.getComponent(this.coords.left());

        if(up!=null){
            if(!up.getConnector(ComponentRotation.U180).compatible(getConnector(ComponentRotation.U000))) return false;
        }
        if(right!=null){
            if(!right.getConnector(ComponentRotation.U270).compatible(getConnector(ComponentRotation.U090))) return false;
        }
        if(down!=null){
            if(!down.getConnector(ComponentRotation.U000).compatible(getConnector(ComponentRotation.U180))) return false;
        }
        if(left!=null){
            if(!left.getConnector(ComponentRotation.U090).compatible(getConnector(ComponentRotation.U270))) return false;
        }
        return true;
    }

    @Override
    public ConnectorType getConnector(ComponentRotation direction){
        int shift = direction.getShift() + (4-this.rotation.getShift());
        shift = shift % 4;
        return connectors[shift];
    }

    @Override
    public ShipCoords getCoords(){
        return this.coords;
    }
    
    @Override
    public boolean powerable(){
        return false;
    }

    @Override
    public void onCreation(iSpaceShip ship){
        return;
    }

    @Override
    public void onDelete(iSpaceShip ship){
        return;
    }

    //ricordate: non implementare questo metodo, ma va implementato in ogni singola sottoclasse
    // (e' letteralmente la def di abstract, ma fa bene ricordarlo).
    @Override
    abstract public void check(iVisitor v);
    
}
