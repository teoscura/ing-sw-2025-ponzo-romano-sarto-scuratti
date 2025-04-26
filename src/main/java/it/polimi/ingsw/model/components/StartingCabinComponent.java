//Done.
package it.polimi.ingsw.model.components;

import it.polimi.ingsw.exceptions.ArgumentTooBigException;
import it.polimi.ingsw.exceptions.NegativeArgumentException;
import it.polimi.ingsw.model.client.components.ClientBaseComponent;
import it.polimi.ingsw.model.client.components.ClientComponent;
import it.polimi.ingsw.model.client.components.ClientCrewComponentDecorator;
import it.polimi.ingsw.model.components.enums.AlienType;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import it.polimi.ingsw.model.components.visitors.iVisitor;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.player.iSpaceShip;

public class StartingCabinComponent extends BaseComponent {
    
    private final PlayerColor color;
    private int crew_number;

    public StartingCabinComponent(int id, 
                          ConnectorType[] connectors, 
                          ComponentRotation rotation,
                          PlayerColor color){
        super(id, connectors, rotation);
        if(color.getOrder()<0) throw new IllegalArgumentException("Color can't be \"NONE\".");
        this.color = color;
        this.crew_number = 2;
    }

    public StartingCabinComponent(int id, 
                          ConnectorType[] connectors, 
                          ComponentRotation rotation,
                          PlayerColor color,
                          ShipCoords coords){
        super(id, connectors, rotation, coords);
        if(color.getOrder()<0) throw new IllegalArgumentException("Color can't be \"NONE\".");
        this.color = color;
        this.crew_number = 2;
    }

    @Override
    public void check(iVisitor v){
        v.visit(this);
    }

    public int getCrew(){
        return crew_number;
    }

    public AlienType getCrewType(){
        return AlienType.HUMAN;
    }

    public PlayerColor getColor(){
        return this.color;
    }

    public void setCrew(iSpaceShip ship, int new_crew, AlienType type){
        if(new_crew<0) throw new NegativeArgumentException("Crew size can't be zero or negative");
        if(type!=AlienType.HUMAN) throw new IllegalArgumentException("Central cabin can only contain humans");
        if(new_crew>AlienType.HUMAN.getMaxCapacity()) throw new ArgumentTooBigException("Crew size exceeds type's max capacity");
        crew_number = new_crew;
    }

    @Override
    public void onCreation(iSpaceShip ship, ShipCoords coords) {
        this.coords = coords;
        ship.addCabinCoords(this.coords);
    }

    @Override
    public void onDelete(iSpaceShip ship) {
        ship.delCabinCoords(this.coords);
    }

    @Override
    public ClientComponent getClientComponent() {
        return new ClientCrewComponentDecorator(new ClientBaseComponent(getID(), getRotation()), AlienType.HUMAN, crew_number);
    }
}
