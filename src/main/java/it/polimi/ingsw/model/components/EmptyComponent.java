package it.polimi.ingsw.model.components;

public class EmptyComponent extends BaseComponent {
    
    public EmptyComponent() throws Exception {
        super(new ConnectorType[4], ComponentRotation.ZERO);
    }

    @Override
    public void check(iVisitor v){
        v.visit(this);
    }
}
