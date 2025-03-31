package it.polimi.ingsw.model.components.visitors;

import it.polimi.ingsw.model.components.*;
import it.polimi.ingsw.model.components.enums.AlienType;

public class CabinVisitor implements iVisitor {
    
    AlienType type = AlienType.HUMAN;

    public AlienType getSupportedType(){
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
        if(this.type==AlienType.BOTH) return;
        if(this.type==AlienType.HUMAN){
            this.type = c.getType();
            return;
        }
        if(this.type!=c.getType()){
            this.type = AlienType.BOTH;
            return;
        }
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

    @Override
    public void visit(StartingCabinComponent c) {
        return;
    }  
}

