package it.polimi.ingsw.model.adventure_cards.visitors;

import it.polimi.ingsw.model.components.AlienLifeSupportComponent;
import it.polimi.ingsw.model.components.BatteryComponent;
import it.polimi.ingsw.model.components.CabinComponent;
import it.polimi.ingsw.model.components.CannonComponent;
import it.polimi.ingsw.model.components.EmptyComponent;
import it.polimi.ingsw.model.components.EngineComponent;
import it.polimi.ingsw.model.components.ShieldComponent;
import it.polimi.ingsw.model.components.StartingCabinComponent;
import it.polimi.ingsw.model.components.StorageComponent;
import it.polimi.ingsw.model.components.StructuralComponent;
import it.polimi.ingsw.model.components.enums.ShipmentType;
import it.polimi.ingsw.model.components.exceptions.ContainerEmptyException;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.components.visitors.iVisitor;

public class ContainsRemoveVisitor implements iVisitor {

    private ShipmentType searching;

    public ContainsRemoveVisitor(ShipmentType type){
        if(type==ShipmentType.EMPTY) throw new IllegalTargetException();
        this.searching = type;
    }

    public ContainsRemoveVisitor(){
        this.searching = null;
    }

    public void changeType(ShipmentType type){
        if(type==ShipmentType.EMPTY) throw new IllegalTargetException();
        this.searching = type;
    }

    @Override
    public void visit(CabinComponent c) {
        throw new IllegalTargetException();
    }

    @Override
    public void visit(EngineComponent c) {
        throw new IllegalTargetException();
    }

    @Override
    public void visit(AlienLifeSupportComponent c) {
        throw new IllegalTargetException();
    }

    @Override
    public void visit(CannonComponent c) {
        throw new IllegalTargetException();
    }

    @Override
    public void visit(StorageComponent c) {
        if(this.searching==null) throw new IllegalTargetException();
        if(c.howMany(searching)<=0) throw new ContainerEmptyException();
        c.takeOut(searching);
    }

    @Override
    public void visit(BatteryComponent c) {
        if(this.searching!=null) throw new IllegalTargetException();
        if(c.getContains()>0){
            c.takeOne();
            return;
        }
        throw new ContainerEmptyException();
    }

    @Override
    public void visit(ShieldComponent c) {
        throw new IllegalTargetException();
    }

    @Override
    public void visit(EmptyComponent c) {
        throw new IllegalTargetException();
    }

    @Override
    public void visit(StructuralComponent c) {
        throw new IllegalTargetException();
    }

    @Override
    public void visit(StartingCabinComponent c) {
        throw new IllegalTargetException();
    }
    
}
