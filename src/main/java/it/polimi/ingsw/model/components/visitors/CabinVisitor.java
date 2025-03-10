package it.polimi.ingsw.model.components.visitors;

import it.polimi.ingsw.model.components.*;

public class CabinVisitor implements iVisitor {
    
    //TODO scrivere funzione che somma tutti i tipi di alieni visitati

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
        //TODO
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
}

