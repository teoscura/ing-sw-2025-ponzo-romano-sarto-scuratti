//Done.
package it.polimi.ingsw.model.components;

import it.polimi.ingsw.model.client.components.ClientBaseComponent;
import it.polimi.ingsw.model.client.components.ClientComponent;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import it.polimi.ingsw.model.components.visitors.iVisitor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.player.iSpaceShip;

public class StructuralComponent extends BaseComponent{

    public StructuralComponent(int id, 
                               ConnectorType[] connectors, 
                               ComponentRotation rotation) {
        super(id, connectors, rotation);
    }
    
    public StructuralComponent(int id, 
                               ConnectorType[] connectors, 
                               ComponentRotation rotation, 
                               ShipCoords coords) {
        super(id, connectors, rotation, coords);
    }

        @Override
    public void check(iVisitor v) {
        v.visit(this);
    }

    @Override
    public void onCreation(iSpaceShip ship) {
        return;
    }

    @Override
    public void onDelete(iSpaceShip ship) {
        return;
    }

    @Override
    public ClientComponent getClientComponent() {
        return new ClientBaseComponent(this.getID(), this.getRotation());
    }
    
}
