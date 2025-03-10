//DONE.
package it.polimi.ingsw.model.components;

import it.polimi.ingsw.model.components.exceptions.ContainerEmptyException;
import it.polimi.ingsw.model.components.exceptions.ContainerFullException;
import it.polimi.ingsw.model.components.visitors.iVisitor;

public class BatteryComponent extends BaseComponent{
    
    private int contains = 0;
    private int max = 2;

    public BatteryComponent(ConnectorType[] connectors, 
                            ComponentRotation rotation, 
                            BatteryType type){
        super(connectors, rotation);
        this.contains = type.getCapacity();
        this.max = type.getCapacity();
    }

    public BatteryComponent(ConnectorType[] connectors, 
                            ComponentRotation rotation, 
                            BatteryType type,
                            int position){
        super(connectors, rotation, position);
        this.contains = type.getCapacity();
        this.max = type.getCapacity();
    }

    @Override
    public void check(iVisitor v){
        v.visit(this);
    }

    public int getContains(){
        return this.contains;
    }

    public int getCapacity(){
        return this.max;
    }

    public void takeOne(){
        if(contains == 0){
            throw new ContainerEmptyException();
        }
        this.contains--;
    }

    public void putOne(){
        if(contains == max){
            throw new ContainerFullException();
        }
        contains++;
    }
}

enum BatteryType {
    DOUBLE (2),
    TRIPLE (3);

    private int capacity;

    BatteryType(int capacity){
        this.capacity = capacity;
    }

    public int getCapacity(){
        return this.capacity;
    }
}
