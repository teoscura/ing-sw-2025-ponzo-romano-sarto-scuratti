//Done.
package it.polimi.ingsw.model.adventure_cards.visitors;

import it.polimi.ingsw.model.components.*;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.components.visitors.iVisitor;

public class CrewRemoveVisitor implements iVisitor {

    @Override
    public void visit(CabinComponent c) {
        if(c.getCrew()==0) return;
        c.setCrew(c.getCrew()-1, c.getCrewType());
    }

    @Override
    public void visit(EngineComponent c) {
        throw new IllegalTargetException("Coords don't correspond to a cabin");
    }

    @Override
    public void visit(AlienLifeSupportComponent c) {
        throw new IllegalTargetException("Coords don't correspond to a cabin");
    }

    @Override
    public void visit(CannonComponent c) {
        throw new IllegalTargetException("Coords don't correspond to a cabin");
    }

    @Override
    public void visit(StorageComponent c) {
        throw new IllegalTargetException("Coords don't correspond to a cabin");
    }

    @Override
    public void visit(BatteryComponent c) {
        throw new IllegalTargetException("Coords don't correspond to a cabin");
    }

    @Override
    public void visit(ShieldComponent c) {
        throw new IllegalTargetException("Coords don't correspond to a cabin");
    }

    @Override
    public void visit(EmptyComponent c) {
        throw new IllegalTargetException("Coords don't correspond to a cabin");
    }

    @Override
    public void visit(StructuralComponent c) {
        throw new IllegalTargetException("Coords don't correspond to a cabin");
    }
    
}
