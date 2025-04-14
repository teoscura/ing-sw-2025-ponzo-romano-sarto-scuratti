package it.polimi.ingsw.model.adventure_cards.visitors;

import it.polimi.ingsw.model.components.*;
import it.polimi.ingsw.model.components.enums.ShipmentType;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.components.visitors.iVisitor;

public class ContainerMoveValidationVisitor implements iVisitor {

    private final ShipmentType searching_for;
    private boolean found = false;

    public ContainerMoveValidationVisitor(ShipmentType type){
        if(type.getValue()==0) throw new IllegalArgumentException();
        this.searching_for = type;
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
        if(!found){
            if(c.howMany(searching_for)==0) throw new IllegalTargetException();
            this.found = true;
            return;
        }
        if(searching_for.getSpecial() && !c.getSpecial()) throw new IllegalTargetException();
        if(c.getFreeSpaces()==0) throw new IllegalTargetException();
    }

    @Override
    public void visit(BatteryComponent c) {
        throw new IllegalTargetException();
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
