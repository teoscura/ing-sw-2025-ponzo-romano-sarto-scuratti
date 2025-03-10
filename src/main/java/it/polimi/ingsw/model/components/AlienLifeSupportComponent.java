//HACK da rivedere
package it.polimi.ingsw.model.components;

import it.polimi.ingsw.exceptions.IllegalConstructorArgumentException;
import it.polimi.ingsw.model.components.visitors.*;

public class AlienLifeSupportComponent extends BaseComponent implements iVisitable {
    
    //FIXME magari usare smartenum e associargli un bonus? boh.

    private AlienType type = AlienType.BROWN;

    public AlienLifeSupportComponent(ConnectorType[] connectors, 
                                ComponentRotation rotation, 
                                AlienType type) 
                                throws Exception {
        super(connectors, rotation);
        if(type==AlienType.BOTH){
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



