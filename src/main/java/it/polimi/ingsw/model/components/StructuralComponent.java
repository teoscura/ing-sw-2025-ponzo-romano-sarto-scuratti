package it.polimi.ingsw.model.components;

import it.polimi.ingsw.model.components.visitors.iVisitor;
import it.polimi.ingsw.model.player.ShipCoords;

public class StructuralComponent extends BaseComponent{

    public StructuralComponent(ConnectorType[] connectors, ComponentRotation rotation) {
        super(connectors, rotation);
    }
    
    public StructuralComponent(ConnectorType[] connectors, ComponentRotation rotation, ShipCoords coords) {
        super(connectors, rotation, coords);
    }

        @Override
    public void check(iVisitor v) {
        v.check(this);
    }
    
}
