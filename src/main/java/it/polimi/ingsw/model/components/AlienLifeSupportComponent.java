//Done.
package it.polimi.ingsw.model.components;

import it.polimi.ingsw.exceptions.IllegalConstructorArgumentException;
import it.polimi.ingsw.model.components.visitors.iVisitor;

public class AlienLifeSupportComponent extends BaseComponent{
    
    private AlienType type = AlienType.BROWN;

    public AlienLifeSupportComponent(ConnectorType[] connectors, 
                                ComponentRotation rotation, 
                                AlienType type){
        super(connectors, rotation);
        if(!type.getNeedLifeSupport()){
            throw new IllegalConstructorArgumentException();
        }
        this.type = type;
    }

    public AlienLifeSupportComponent(ConnectorType[] connectors, 
                                ComponentRotation rotation, 
                                AlienType type,
                                int position){
        super(connectors, rotation, position);
        if(!type.getNeedLifeSupport()){
            throw new IllegalConstructorArgumentException();
        }
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



