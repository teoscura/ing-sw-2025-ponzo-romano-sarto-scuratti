//Done.
package it.polimi.ingsw.model.components;

import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import it.polimi.ingsw.model.components.visitors.iVisitor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.player.iSpaceShip;



public interface iBaseComponent {
    
    //Connectors are stored in this order: UP, RIGHT, DOWN, LEFT;
    
    public boolean verify(iSpaceShip state);
    public void check(iVisitor v);
    //On delete/Creation
    public void onCreation(iSpaceShip state);
    public void onDelete(iSpaceShip state);
    //Return the connector pointing up.
    public ShipCoords getCoords();
    public ConnectorType getConnector(ComponentRotation direction);
    public ConnectorType[] getConnectors();
    public ComponentRotation getRotation();
    public boolean powerable();
}