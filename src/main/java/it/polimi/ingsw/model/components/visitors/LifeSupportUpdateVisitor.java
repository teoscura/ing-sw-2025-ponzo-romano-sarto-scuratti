package it.polimi.ingsw.model.components.visitors;

import it.polimi.ingsw.model.components.*;
import it.polimi.ingsw.model.components.enums.AlienType;

public class LifeSupportUpdateVisitor implements iVisitor {

    private final AlienType type;
    private boolean still_alive = false;

    public LifeSupportUpdateVisitor(AlienType type){
        this.type = type;
    }

    public boolean getStillAlive(){
        return this.still_alive;
    }

    @Override
    public void visit(CabinComponent c) {
        if(c.getCrewType()!=type) still_alive = true;
    }

    @Override
    public void visit(EngineComponent c) {
        return;
    }

    @Override
    public void visit(AlienLifeSupportComponent c) {
        if(c.getType()==this.type) still_alive = true;
    }

    @Override
    public void visit(CannonComponent c) {
        return;
    }

    @Override
    public void visit(StorageComponent c) {
        return;
    }

    @Override
    public void visit(BatteryComponent c) {
        return;
    }

    @Override
    public void visit(ShieldComponent c) {
        return;
    }

    @Override
    public void visit(EmptyComponent c) {
        return;
    }

    @Override
    public void visit(StructuralComponent c) {
        return;
    }

    @Override
    public void visit(StartingCabinComponent c) {
        return;
    }
    
}
