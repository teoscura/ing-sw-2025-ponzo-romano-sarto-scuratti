package it.polimi.ingsw.model.components.visitors;

import it.polimi.ingsw.model.components.*;
import it.polimi.ingsw.model.components.enums.AlienType;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.player.iSpaceShip;

public class CrewSetVisitor implements iVisitor {

    private final iSpaceShip ship;
    private final AlienType type;

    public CrewSetVisitor(iSpaceShip ship, AlienType type){
        if(ship==null) throw new NullPointerException();
        if(type.getArraypos()<0&&type.getMaxCapacity()>0) throw new IllegalArgumentException();
        this.ship = ship;
        this.type = type;
    }

    @Override
    public void visit(CabinComponent c) {
        c.setCrew(ship, type.getMaxCapacity(), type);
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
        throw new IllegalTargetException();
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
