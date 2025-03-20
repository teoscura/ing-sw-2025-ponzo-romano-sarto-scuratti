//Done.
package it.polimi.ingsw.model.components;

import java.util.Arrays;

import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import it.polimi.ingsw.model.components.visitors.iVisitor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.player.iSpaceShip;

public class EmptyComponent extends BaseComponent {
    
    public EmptyComponent(){
        super(new ConnectorType[4], ComponentRotation.U000);
    }

    public EmptyComponent(ShipCoords coords){
        super(new ConnectorType[4], ComponentRotation.U000, coords);
    }

    @Override
    public boolean verify(iSpaceShip ship){
        return true;
    }

    @Override
    public void check(iVisitor v){
        v.visit(this);
    }

    @Override
    public ConnectorType[] getConnectors(){
        ConnectorType[] tmp = new ConnectorType[4];
        Arrays.fill(tmp, ConnectorType.EMPTY);
        return tmp;
    }
}
