//Done.
package it.polimi.ingsw.model.components;

import it.polimi.ingsw.exceptions.ArgumentTooBigException;
import it.polimi.ingsw.exceptions.NegativeArgumentException;
import it.polimi.ingsw.model.components.enums.AlienType;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import it.polimi.ingsw.model.components.exceptions.UnsupportedAlienCabinException;
import it.polimi.ingsw.model.components.visitors.CabinVisitor;
import it.polimi.ingsw.model.components.visitors.iVisitor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.player.iSpaceShip;

public class CabinComponent extends BaseComponent{
    
    private int crew_number = 0;
    private AlienType crew_type = AlienType.HUMAN;

    public CabinComponent(ConnectorType[] connectors, 
                          ComponentRotation rotation){
        super(connectors, rotation);
    }

    public CabinComponent(ConnectorType[] connectors, 
                          ComponentRotation rotation,
                          ShipCoords coords){
        super(connectors, rotation, coords);
    }

    @Override
    public void check(iVisitor v){
        v.visit(this);
    }

    public int getCrew(){
        return crew_number;
    }

    public AlienType getCrewType(){
        return this.crew_type;
    }

    public void setCrew(iSpaceShip ship, int new_crew, AlienType type){
        if(new_crew<=0) throw new NegativeArgumentException("Crew size can't be zero or negative");
        if(type.getArraypos()<0) throw new IllegalArgumentException("Type must be a single alien type, not a collector");
        if(new_crew>type.getMaxCapacity()) throw new ArgumentTooBigException("Crew size exceeds type's max capacity");
        CabinVisitor v = new CabinVisitor();
        for(iBaseComponent c : this.getConnectedComponents(ship)){
            c.check(v);
        }
        if(!type.compatible(v.getSupportedType())) throw new UnsupportedAlienCabinException("Tried to insert crew type in cabin that doesn't support it.");
        crew_number = new_crew;
        crew_type = type;
    }

    @Override
    public void onCreation(iSpaceShip ship) {
        ship.addCabinCoords(this.coords);
    }

    @Override
    public void onDelete(iSpaceShip ship) {
        ship.delCabinCoords(this.coords);
    }
}

