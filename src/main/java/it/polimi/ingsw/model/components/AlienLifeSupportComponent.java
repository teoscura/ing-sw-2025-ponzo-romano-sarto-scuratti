//Done.
package it.polimi.ingsw.model.components;

import it.polimi.ingsw.exceptions.IllegalConstructorArgumentException;
import it.polimi.ingsw.model.components.enums.AlienType;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import it.polimi.ingsw.model.components.visitors.iVisitor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.player.iSpaceShip;

public class AlienLifeSupportComponent extends BaseComponent{
    
    private AlienType type = AlienType.BROWN;

    public AlienLifeSupportComponent(ConnectorType[] connectors, 
                                ComponentRotation rotation, 
                                AlienType type){
        super(connectors, rotation);
        if(!type.getLifeSupportExists()){
            throw new IllegalConstructorArgumentException();
        }
        this.type = type;
    }

    public AlienLifeSupportComponent(ConnectorType[] connectors, 
                                ComponentRotation rotation, 
                                AlienType type,
                                ShipCoords coords){
        super(connectors, rotation, coords);
        if(!type.getLifeSupportExists()){
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

    @Override
    public void onCreation(iSpaceShip ship) {
        return;
    }

    @Override
    public void onDelete(iSpaceShip ship) {
        return;
    }
    
}



