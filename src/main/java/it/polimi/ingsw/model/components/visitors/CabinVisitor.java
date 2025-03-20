package it.polimi.ingsw.model.components.visitors;

import it.polimi.ingsw.model.components.*;
import it.polimi.ingsw.model.components.enums.AlienType;

public class CabinVisitor implements iVisitor {
    
    AlienType type = AlienType.HUMAN;

    public AlienType getType(){
        return this.type;
    }

    @Override
    public void visit(CabinComponent c){
        return;
    }

    @Override
    public void visit(EngineComponent c){
        return;
    }

    @Override
    public void visit(AlienLifeSupportComponent c){
        this.type = c.getType();
    }

    @Override
    public void visit(CannonComponent c){
        return;
    }

    @Override
    public void visit(StorageComponent c){
        return;
    }

    @Override
    public void visit(BatteryComponent c){
        return;
    }

    @Override
    public void visit(ShieldComponent c){
        return;
    }

    @Override
    public void visit(EmptyComponent c){
        return;
    }

    @Override
    public void visit(StructuralComponent c) {
        return;
    }

    public void reset(){
        this.type = AlienType.HUMAN;
    }    
}

