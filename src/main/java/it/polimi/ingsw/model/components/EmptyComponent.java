package it.polimi.ingsw.model.components;

import java.util.Arrays;

import it.polimi.ingsw.model.components.visitors.iVisitor;

public class EmptyComponent extends BaseComponent {
    
    public EmptyComponent() throws Exception {
        super(new ConnectorType[4], ComponentRotation.ZERO);
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
