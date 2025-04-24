//Done.
package it.polimi.ingsw.model.components;

import java.io.Serializable;

import it.polimi.ingsw.model.client.components.ClientComponent;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import it.polimi.ingsw.model.components.visitors.iVisitor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.player.iSpaceShip;



public interface iBaseComponent extends Serializable {
    
    //Connectors are stored in this order: UP, RIGHT, DOWN, LEFT;
    
    public int getID();
    public boolean verify(iSpaceShip ship);
    public void check(iVisitor v);
    //On delete/Creation
    public void onCreation(iSpaceShip ship);
    public void onDelete(iSpaceShip ship);
    //Return the connector pointing up.
    public ShipCoords getCoords();
    public ConnectorType getConnector(ComponentRotation direction);
    public ConnectorType[] getConnectors();
    public ComponentRotation getRotation();
    public iBaseComponent[] getConnectedComponents(iSpaceShip ship);
    public boolean powerable();
    public ClientComponent getClientComponent();
}