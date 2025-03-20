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


//FIXME huge refactor if possible, really dirty implementation.
public class CabinComponent extends BaseComponent{
    private int max_capacity = AlienType.HUMAN.getMaxCapacity();
    private int crew_number = 0;
    private AlienType crew_type = AlienType.HUMAN;
    private AlienType can_contain = AlienType.HUMAN;

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

    public void setCrew(int new_crew, AlienType type){
        if(type==AlienType.HUMAN){
            crew_type = type;
        }
        else if(type==AlienType.BROWN && (this.can_contain==AlienType.BOTH || this.can_contain==AlienType.BROWN)){
            crew_type = AlienType.BROWN;
        }
        else if(type==AlienType.PURPLE && (this.can_contain==AlienType.BOTH || this.can_contain==AlienType.PURPLE)){
            crew_type = AlienType.PURPLE;
        }
        else throw new UnsupportedAlienCabinException();
        if(new_crew<0){
            throw new NegativeArgumentException();
        }
        if(new_crew>max_capacity){
            throw new ArgumentTooBigException();
        }
        this.crew_number = new_crew;
    }

    private void upgradeCrewType(AlienType type){
        if(this.can_contain == AlienType.BOTH) return;
        if(type==AlienType.BOTH || type==AlienType.HUMAN) throw new IllegalArgumentException();
        if(this.can_contain == AlienType.HUMAN){
            this.can_contain = type;
            this.max_capacity = type.getMaxCapacity();
            return;
        }
        if(this.can_contain!=type){
            this.can_contain = AlienType.BOTH;
        }
    }
    
    public void updateCrewType(iSpaceShip ship){
        //FIXME
        CabinVisitor v = new CabinVisitor();
        iBaseComponent up = ship.getComponent(this.coords.up());
        iBaseComponent right = ship.getComponent(this.coords.right());
        iBaseComponent down = ship.getComponent(this.coords.down());
        iBaseComponent left = ship.getComponent(this.coords.left());
        if(up.getConnector(ComponentRotation.U180).connected(this.getConnector(ComponentRotation.U000))){
            up.check(v);
            this.upgradeCrewType(v.getType());
            v.reset();
        }
        if(right.getConnector(ComponentRotation.U270).connected(this.getConnector(ComponentRotation.U090))){
            right.check(v);
            this.upgradeCrewType(v.getType());
            v.reset(); 
        }
        if(down.getConnector(ComponentRotation.U000).connected(this.getConnector(ComponentRotation.U180))){
            down.check(v);
            this.upgradeCrewType(v.getType());
            v.reset();
        }
        if(left.getConnector(ComponentRotation.U090).connected(this.getConnector(ComponentRotation.U270))){
            left.check(v);
            this.upgradeCrewType(v.getType());
            v.reset();
        }
    }
}

