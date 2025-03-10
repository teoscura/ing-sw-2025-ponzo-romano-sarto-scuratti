package it.polimi.ingsw.model.components;

import it.polimi.ingsw.exceptions.ArgumentTooBigException;
import it.polimi.ingsw.exceptions.NegativeArgumentException;
import it.polimi.ingsw.model.components.visitors.iVisitor;

public class CabinComponent extends BaseComponent{
    //HACK probabilmente devo sistemare il modo in cui la crew viene gestita, troppo ad cazzum e difficile da maneggiare.
    private int max_capacity;
    private int crew_number = 0;
    private AlienType crew_type;

    public CabinComponent(ConnectorType[] connectors, 
                          ComponentRotation rotation,
                          AlienType inhabitant_type)
                          throws Exception {
        super(connectors, rotation);
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
    
    public void updateCrewType(){
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

