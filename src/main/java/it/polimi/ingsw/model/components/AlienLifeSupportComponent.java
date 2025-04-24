//Done.
package it.polimi.ingsw.model.components;

import java.util.Arrays;
import java.util.List;

import it.polimi.ingsw.exceptions.IllegalConstructorArgumentException;
import it.polimi.ingsw.model.adventure_cards.visitors.CrewRemoveVisitor;
import it.polimi.ingsw.model.client.components.ClientBaseComponent;
import it.polimi.ingsw.model.client.components.ClientComponent;
import it.polimi.ingsw.model.components.enums.AlienType;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.components.visitors.LifeSupportUpdateVisitor;
import it.polimi.ingsw.model.components.visitors.iVisitor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.player.iSpaceShip;

public class AlienLifeSupportComponent extends BaseComponent {
    
    private AlienType type = AlienType.BROWN;

    public AlienLifeSupportComponent(int id, 
                                ConnectorType[] connectors, 
                                ComponentRotation rotation, 
                                AlienType type){
        super(id, connectors, rotation);
        if(!type.getLifeSupportExists()){
            throw new IllegalConstructorArgumentException();
        }
        this.type = type;
    }

    public AlienLifeSupportComponent(int id, 
                                ConnectorType[] connectors, 
                                ComponentRotation rotation, 
                                AlienType type,
                                ShipCoords coords){
        super(id, connectors, rotation, coords);
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
        iBaseComponent[] tmp = this.getConnectedComponents(ship);
        for(iBaseComponent c : tmp){
            if(!ship.isCabin(c.getCoords())) continue;
            LifeSupportUpdateVisitor v = new LifeSupportUpdateVisitor(this.type);
            c.check(v);
            if(v.getStillAlive()) continue;
            List<iBaseComponent> to_check = Arrays.asList(c.getConnectedComponents(ship));
            to_check.remove(this);
            for(iBaseComponent s : to_check){
                s.check(v);
            }
            if(v.getStillAlive()) continue;
            CrewRemoveVisitor cr = new CrewRemoveVisitor(ship);
            try {
                c.check(cr);
            } catch (IllegalTargetException e){
                //crew was already empty, so we ignore this exception;
            }
        }
    }

    @Override
    public ClientComponent getClientComponent() {
        return new ClientBaseComponent(this.getID(), this.getRotation());
    }
    
}



