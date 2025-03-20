package it.polimi.ingsw.model.adventure_cards.visitors;

import it.polimi.ingsw.model.components.*;
import it.polimi.ingsw.model.components.visitors.iVisitor;

public class EpidemicVisitor implements iVisitor{

    @Override
    public void visit(CabinComponent c) {
        //TODO.
    }

    @Override
    public void visit(EngineComponent c) {
        return;
    }

    @Override
    public void visit(AlienLifeSupportComponent c) {
        return;
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
    
}
