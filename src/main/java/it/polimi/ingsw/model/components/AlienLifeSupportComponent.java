package it.polimi.ingsw.model.components;

public class AlienLifeSupportComponent extends BaseComponent{
    
    private AlienType type = AlienType.BROWN;

    public AlienLifeSupportComponent(ConnectorType[] connectors, 
                                ComponentRotation rotation, 
                                AlienType type) 
                                throws Exception {
        super(connectors, rotation);
        this.type = type;
    }

    @Override
    public void check(iVisitor v){
        v.visit(this);
    }

    public AlienType getType(){
        return type;
    }

}



