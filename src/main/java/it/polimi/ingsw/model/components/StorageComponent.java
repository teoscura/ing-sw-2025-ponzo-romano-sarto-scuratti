//Done.
package it.polimi.ingsw.model.components;

import java.util.Arrays;

import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import it.polimi.ingsw.model.components.enums.ShipmentType;
import it.polimi.ingsw.model.components.enums.StorageType;
import it.polimi.ingsw.model.components.exceptions.ContainerFullException;
import it.polimi.ingsw.model.components.exceptions.ContainerNotSpecialException;
import it.polimi.ingsw.model.components.visitors.iVisitor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.player.iSpaceShip;


//FIXME: rewrite this. this is horrible.
public class StorageComponent extends BaseComponent{

    private ShipmentType[] shipments;
    private int currently_full = 0;
    private boolean special = false;

    public StorageComponent(int id, 
                            ConnectorType[] connectors, 
                            ComponentRotation rotation,
                            StorageType type){
        super(id, connectors, rotation);
        this.special = type.getSpecial();
        this.shipments = new ShipmentType[type.getCapacity()];
        Arrays.fill(shipments, ShipmentType.EMPTY);
    }

    public StorageComponent(int id, 
                            ConnectorType[] connectors, 
                            ComponentRotation rotation,
                            StorageType type,
                            ShipCoords coords){
        super(id, connectors, rotation, coords);
        this.special = type.getSpecial();
        this.shipments = new ShipmentType[type.getCapacity()];
        Arrays.fill(shipments, ShipmentType.EMPTY);
    }

    @Override
    public void check(iVisitor v){
        v.visit(this);
    }

    public void putIn(ShipmentType shipment){
        if(shipment==null) throw new NullPointerException();
        if(currently_full==getCapacity()){
            throw new ContainerFullException();
        }
        if(this.special==false && shipment.getSpecial()==true){
            throw new ContainerNotSpecialException();
        }
        for(int i=0;i<getCapacity();i++){
            if(shipments[i]!=ShipmentType.EMPTY){
                continue;
            }
            shipments[i]=shipment;
            currently_full++;
            return;
        }
    }

    public boolean takeOut(ShipmentType container){
        if(container==null) throw new NullPointerException();
        for(int i=0;i<getCapacity();i++){
            if(shipments[i] == container){
                shipments[i] = ShipmentType.EMPTY;
                currently_full--;
                return true;
            }
        }
        return false;
    }

    public int howMany(ShipmentType container){
        if(container==null) throw new NullPointerException();
        int tmp = 0;
        for(int i=0; i<getCapacity(); i++){
            if(shipments[i] == container){
                tmp++;
            }
        }
        return tmp;
    }

    public int getFreeSpaces(){
        return getCapacity() - this.currently_full;
    }

    public boolean getSpecial(){
        return this.special;
    }

    public int getCapacity(){
        return shipments.length;
    }

    @Override
    public void onCreation(iSpaceShip ship) {
        ship.addStorageCoords(this.coords);
    }

    @Override
    public void onDelete(iSpaceShip ship) {
        ship.delStorageCoords(this.coords);
    }

}