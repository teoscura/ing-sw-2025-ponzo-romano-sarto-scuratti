package it.polimi.ingsw.model.components;

import it.polimi.ingsw.exceptions.ArgumentTooBigException;
import it.polimi.ingsw.exceptions.NegativeArgumentException;
import it.polimi.ingsw.model.components.visitors.CabinVisitor;
import it.polimi.ingsw.model.components.visitors.iVisitor;
import it.polimi.ingsw.model.player.iSpaceShip;

public class CabinComponent extends BaseComponent{
    private int max_capacity;
    private int crew_number = 0;
    private AlienType crew_type;

    public CabinComponent(ConnectorType[] connectors, 
                          ComponentRotation rotation,
                          AlienType inhabitant_type){
        super(connectors, rotation);
        this.max_capacity = inhabitant_type.getMaxCapacity(); 
    }

    public CabinComponent(ConnectorType[] connectors, 
                          ComponentRotation rotation,
                          AlienType inhabitant_type,
                          int position){
        super(connectors, rotation, position);
        this.max_capacity = inhabitant_type.getMaxCapacity(); 
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

    public void setCrew(int new_crew){
        if(new_crew<0){
            throw new NegativeArgumentException();
        }
        if(new_crew>max_capacity){
            throw new ArgumentTooBigException();
        }
        this.crew_number = new_crew;
    }
    
    public void updateCrewType(iSpaceShip state, int position){
        //TODO: chiedere a ponzo se deve essere connesso coi connettori o no.
        iVisitor v = new CabinVisitor();
        state.getComponent(state.up(position)).check(v);
        state.getComponent(state.down(position)).check(v);
        state.getComponent(state.left(position)).check(v);
        state.getComponent(state.right(position)).check(v);
        //TODO: creare un visitor speciale per checkare i possibili tipi alieni che puo' contenere.
    }
}

enum AlienType{
    HUMAN (2, false),
    BROWN (1, true),
    PURPLE (1, true),
    BOTH (1, false);//Cabina collegata sia a support viola che marrone

    private int max_capacity;
    private boolean need_lifesupport;

    AlienType(int max_capacity, boolean need_lifesupport){
        this.max_capacity = max_capacity;
        this.need_lifesupport = need_lifesupport;
    }

    public int getMaxCapacity(){
        return this.max_capacity;
    }

    public boolean getNeedLifeSupport(){
        return this.need_lifesupport;
    }
}

